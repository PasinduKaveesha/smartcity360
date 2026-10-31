package lk.jiat.smartcity360.energy;

import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Decorator appending an HMAC-SHA256 tag over the transmitted packet for integrity verification.
 */
public class AuthenticationLayer extends StreamLayer {

    public AuthenticationLayer(MeterStream inner) {
        super(inner);
    }

    @Override
    public String getDescription() {
        return inner.getDescription() + " + Authentication";
    }

    @Override
    public byte[] transmit(String reading) {
        byte[] payload = inner.transmit(reading);
        byte[] hmac = computeHmac(payload);
        byte[] packet = new byte[payload.length + hmac.length];
        System.arraycopy(payload, 0, packet, 0, payload.length);
        System.arraycopy(hmac, 0, packet, payload.length, hmac.length);
        return packet;
    }

    @Override
    public String receive(byte[] packet) {
        if (packet == null || packet.length < EnergyConstants.HMAC_TAG_LENGTH_BYTES) {
            throw new StreamAuthenticationException("Packet too short to contain HMAC-SHA256 tag");
        }
        int payloadLen = packet.length - EnergyConstants.HMAC_TAG_LENGTH_BYTES;
        byte[] payload = new byte[payloadLen];
        byte[] tag = new byte[EnergyConstants.HMAC_TAG_LENGTH_BYTES];
        System.arraycopy(packet, 0, payload, 0, payloadLen);
        System.arraycopy(packet, payloadLen, tag, 0, EnergyConstants.HMAC_TAG_LENGTH_BYTES);

        byte[] expected = computeHmac(payload);
        if (!MessageDigest.isEqual(expected, tag)) {
            throw new StreamAuthenticationException("HMAC verification failed: packet altered or corrupted");
        }
        return inner.receive(payload);
    }

    private byte[] computeHmac(byte[] data) {
        try {
            Mac mac = Mac.getInstance(EnergyConstants.HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(StreamSecurityConfig.getHmacKey(), EnergyConstants.HMAC_ALGORITHM));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new IllegalStateException("Failed computing HMAC-SHA256 tag", e);
        }
    }
}
