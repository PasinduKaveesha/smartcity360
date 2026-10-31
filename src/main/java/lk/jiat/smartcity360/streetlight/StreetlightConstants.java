package lk.jiat.smartcity360.streetlight;

/**
 * Constants governing streetlight brightness ranges, memory estimation, and batch processing.
 */
public final class StreetlightConstants {

    public static final int MIN_BRIGHTNESS = 0;
    public static final int MAX_BRIGHTNESS = 100;
    public static final int MAX_BULK_COUNT = 20000;
    public static final int DEFAULT_BATCH_SIZE = 500;

    // Memory footprint estimates (in bytes) for Flyweight pattern demonstration
    public static final long MODEL_MEMORY_BYTES = 256;
    public static final long LIGHT_EXTRINSIC_MEMORY_BYTES = 64;
    public static final long LIGHT_UNSHARED_MEMORY_BYTES = 320; // Extrinsic + Duplicate Intrinsic

    private StreetlightConstants() {
    }
}
