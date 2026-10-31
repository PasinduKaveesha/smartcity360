package lk.jiat.smartcity360.energy;

/**
 * Component interface defining the contract for transmitting and receiving energy meter stream packets.
 */
public interface MeterStream {

    String getDescription();

    byte[] transmit(String reading);

    String receive(byte[] packet);
}
