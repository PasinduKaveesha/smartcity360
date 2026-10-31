package lk.jiat.smartcity360.energy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Decorator compressing transmitted stream payload using GZIP.
 */
public class CompressionLayer extends StreamLayer {

    public CompressionLayer(MeterStream inner) {
        super(inner);
    }

    @Override
    public String getDescription() {
        return inner.getDescription() + " + Compression";
    }

    @Override
    public byte[] transmit(String reading) {
        byte[] payload = inner.transmit(reading);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
            gzos.write(payload);
            gzos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed compressing payload with GZIP", e);
        }
    }

    @Override
    public String receive(byte[] packet) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(packet);
             GZIPInputStream gzis = new GZIPInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            gzis.transferTo(baos);
            return inner.receive(baos.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Failed decompressing GZIP packet", e);
        }
    }
}
