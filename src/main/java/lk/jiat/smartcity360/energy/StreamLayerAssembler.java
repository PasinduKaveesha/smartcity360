package lk.jiat.smartcity360.energy;

import java.util.List;

/**
 * Assembler wrapping a base MeterStream in decorator layers matching a specified sequence.
 */
public final class StreamLayerAssembler {

    private StreamLayerAssembler() {
    }

    public static MeterStream assemble(MeterStream base, List<LayerType> layers) {
        if (base == null) {
            throw new IllegalArgumentException("Base stream cannot be null");
        }
        if (layers == null || layers.isEmpty()) {
            return base;
        }

        MeterStream current = base;
        for (LayerType layerType : layers) {
            if (layerType == null) {
                continue;
            }
            switch (layerType) {
                case ENCRYPTION -> current = new EncryptionLayer(current);
                case AUTHENTICATION -> current = new AuthenticationLayer(current);
                case COMPRESSION -> current = new CompressionLayer(current);
            }
        }
        return current;
    }
}
