package lk.jiat.smartcity360.energy;

/**
 * Constants governing energy metering stream security, encryption, and compression.
 */
public final class EnergyConstants {

    public static final String SYSTEM_PROPERTY_KEY = "smartcity360.stream.key";
    public static final String DEFAULT_AES_KEY_BASE64 = "u1P7xR3qZ9wM2kL5vN8bT0jH4yG6fD1s"; // 32 chars = 256 bits UTF-8
    public static final String DEFAULT_HMAC_KEY_BASE64 = "k8M2vL5vN8bT0jH4yG6fD1su1P7xR3qZ"; // 32 chars = 256 bits UTF-8

    public static final int GCM_IV_LENGTH_BYTES = 12;
    public static final int GCM_TAG_LENGTH_BITS = 128;
    public static final int HMAC_TAG_LENGTH_BYTES = 32;

    public static final String AES_GCM_TRANSFORMATION = "AES/GCM/NoPadding";
    public static final String HMAC_ALGORITHM = "HmacSHA256";

    private EnergyConstants() {
    }
}
