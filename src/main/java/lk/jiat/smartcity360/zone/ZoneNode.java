package lk.jiat.smartcity360.zone;

/**
 * Component interface defining the contract for both composite zones and leaf buildings.
 */
public interface ZoneNode {

    String getName();

    ZoneLevel getLevel();

    double getEnergyUsageKwh();

    boolean isPowered();

    void shutdownPower();

    void restorePower();

    String reportUsage();
}
