package lk.jiat.smartcity360.energy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests verifying the Decorator pattern implementation for energy meter streams.
 */
class EnergyStreamTest {

    @Test
    @DisplayName("Raw stream round-trip transmission without decorators")
    void testRawRoundTrip() {
        MeterStream stream = new RawMeterStream();
        String original = "123.456";
        byte[] packet = stream.transmit(original);
        String received = stream.receive(packet);
        assertEquals(original, received);
        assertEquals("Raw meter stream", stream.getDescription());
    }

    @Test
    @DisplayName("Round-trip succeeds for all combinations and orderings of decorators")
    void testAllLayerCombinations() {
        List<List<LayerType>> combinations = List.of(
                List.of(),
                List.of(LayerType.ENCRYPTION),
                List.of(LayerType.AUTHENTICATION),
                List.of(LayerType.COMPRESSION),
                List.of(LayerType.ENCRYPTION, LayerType.AUTHENTICATION),
                List.of(LayerType.AUTHENTICATION, LayerType.ENCRYPTION),
                List.of(LayerType.ENCRYPTION, LayerType.COMPRESSION),
                List.of(LayerType.COMPRESSION, LayerType.ENCRYPTION),
                List.of(LayerType.AUTHENTICATION, LayerType.COMPRESSION),
                List.of(LayerType.COMPRESSION, LayerType.AUTHENTICATION),
                List.of(LayerType.ENCRYPTION, LayerType.AUTHENTICATION, LayerType.COMPRESSION),
                List.of(LayerType.COMPRESSION, LayerType.AUTHENTICATION, LayerType.ENCRYPTION),
                List.of(LayerType.AUTHENTICATION, LayerType.ENCRYPTION, LayerType.COMPRESSION)
        );

        String sampleReading = "458.925_KWH_ZONE_CENTRAL_METER_EM_01";
        for (List<LayerType> layers : combinations) {
            MeterStream pipeline = StreamLayerAssembler.assemble(new RawMeterStream(), layers);
            byte[] packet = pipeline.transmit(sampleReading);
            String recovered = pipeline.receive(packet);
            assertEquals(sampleReading, recovered, "Failed round-trip for layers: " + layers);
        }
    }

    @Test
    @DisplayName("Tampered packet throws StreamAuthenticationException")
    void testTamperedPacketFailsAuthentication() {
        MeterStream pipeline = StreamLayerAssembler.assemble(new RawMeterStream(), List.of(LayerType.AUTHENTICATION));
        String sample = "100.50";
        byte[] packet = pipeline.transmit(sample);

        // Corrupt first byte of payload
        packet[0] = (byte) (packet[0] ^ 0xFF);

        assertThrows(StreamAuthenticationException.class, () -> pipeline.receive(packet));
    }

    @Test
    @DisplayName("Compression layer shrinks repetitive telemetry payloads")
    void testCompressionShrinksRepetitiveInput() {
        MeterStream raw = new RawMeterStream();
        MeterStream compressed = StreamLayerAssembler.assemble(new RawMeterStream(), List.of(LayerType.COMPRESSION));

        String repetitiveData = "TELEMETRY_READING_TIMESTAMP_2025_ENERGY_CONSUMPTION_KWH_".repeat(30);

        byte[] rawBytes = raw.transmit(repetitiveData);
        byte[] compressedBytes = compressed.transmit(repetitiveData);

        assertTrue(compressedBytes.length < rawBytes.length,
                String.format("Compressed (%d) should be smaller than raw (%d)", compressedBytes.length, rawBytes.length));

        String decompressed = compressed.receive(compressedBytes);
        assertEquals(repetitiveData, decompressed);
    }
}
