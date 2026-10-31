package lk.jiat.smartcity360.energy;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Decorator adding AES/GCM authenticated encryption with a prepended 12-byte IV.
 */
public class EncryptionLayer extends StreamLayer {

    private static final SecureRandom RANDOM = new SecureRandom();

    public EncryptionLayer(MeterStream inner) {
        super(inner);
    }

    @Override
    public String getDescription() {
        return inner.getDescription() + " + Encryption";
    }

    @Override
    public byte[] transmit(String reading) {
        byte[] plainBytes = inner.transmit(reading);
        try {
            byte[] iv = new byte[EnergyConstants.GCM_IV_LENGTH_BYTES];
            RANDOM.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(EnergyConstants.AES_GCM_TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(StreamSecurityConfig.getAesKey(), "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(EnergyConstants.GCM_TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
            byte[] encrypted = cipher.doFinal(plainBytes);

            byte[] packet = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, packet, 0, iv.length);
            System.arraycopy(encrypted, 0, packet, iv.length, encrypted.length);
            return packet;
        } catch (Exception e) {
            throw new IllegalStateException("Failed encrypting stream payload", e);
        }
    }

    @Override
    public String receive(byte[] packet) {
        if (packet == null || packet.length < EnergyConstants.GCM_IV_LENGTH_BYTES) {
            throw new IllegalArgumentException("Packet is too short for AES/GCM decryption");
        }
        try {
            byte[] iv = new byte[EnergyConstants.GCM_IV_LENGTH_BYTES];
            System.arraycopy(packet, 0, iv, 0, iv.length);

            int cipherLen = packet.length - iv.length;
            byte[] cipherBytes = new byte[cipherLen];
            System.arraycopy(packet, iv.length, cipherBytes, 0, cipherLen);

            Cipher cipher = Cipher.getInstance(EnergyConstants.AES_GCM_TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(StreamSecurityConfig.getAesKey(), "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(EnergyConstants.GCM_TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
            byte[] decrypted = cipher.doFinal(cipherBytes);
            return inner.receive(decrypted);
        } catch (Exception e) {
            throw new IllegalStateException("Failed decrypting stream payload", e);
        }
    }
}
