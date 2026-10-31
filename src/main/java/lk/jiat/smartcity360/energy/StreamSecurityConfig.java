package lk.jiat.smartcity360.energy;

import java.nio.charset.StandardCharsets;

/**
 * Security configuration helper resolving encryption and authentication keys.
 */
public final class StreamSecurityConfig {

    private StreamSecurityConfig() {
    }

    public static byte[] getAesKey() {
        String sysProp = System.getProperty(EnergyConstants.SYSTEM_PROPERTY_KEY);
        if (sysProp != null && !sysProp.isBlank()) {
            return normalizeKey(sysProp.getBytes(StandardCharsets.UTF_8));
        }
        return EnergyConstants.DEFAULT_AES_KEY_BASE64.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] getHmacKey() {
        return EnergyConstants.DEFAULT_HMAC_KEY_BASE64.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] normalizeKey(byte[] raw) {
        byte[] key = new byte[32];
        System.arraycopy(raw, 0, key, 0, Math.min(raw.length, 32));
        return key;
    }
}
