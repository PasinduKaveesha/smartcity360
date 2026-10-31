package lk.jiat.smartcity360.energy;

import java.nio.charset.StandardCharsets;

/**
 * Concrete component providing the fundamental raw byte transmission layer.
 */
public class RawMeterStream implements MeterStream {

    @Override
    public String getDescription() {
        return "Raw meter stream";
    }

    @Override
    public byte[] transmit(String reading) {
        if (reading == null) {
            throw new IllegalArgumentException("Reading cannot be null");
        }
        return reading.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String receive(byte[] packet) {
        if (packet == null) {
            throw new IllegalArgumentException("Packet cannot be null");
        }
        return new String(packet, StandardCharsets.UTF_8);
    }
}
