package lk.jiat.smartcity360.zone;

/**
 * Leaf node representing an individual building in the city zone hierarchy.
 */
public class Building implements ZoneNode {

    private Long id;
    private final String name;
    private double baseUsageKwh;
    private boolean powered;

    public Building(Long id, String name, double baseUsageKwh, boolean powered) {
        this.id = id;
        this.name = name;
        this.baseUsageKwh = baseUsageKwh;
        this.powered = powered;
    }

    public Building(String name, double baseUsageKwh, boolean powered) {
        this(null, name, baseUsageKwh, powered);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ZoneLevel getLevel() {
        return ZoneLevel.BUILDING;
    }

    @Override
    public double getEnergyUsageKwh() {
        return powered ? baseUsageKwh : 0.0;
    }

    public double getBaseUsageKwh() {
        return baseUsageKwh;
    }

    public void setBaseUsageKwh(double baseUsageKwh) {
        this.baseUsageKwh = baseUsageKwh;
    }

    @Override
    public boolean isPowered() {
        return powered;
    }

    @Override
    public void shutdownPower() {
        this.powered = false;
    }

    @Override
    public void restorePower() {
        this.powered = true;
    }

    @Override
    public String reportUsage() {
        return String.format("%s [BUILDING] - Powered: %s, Usage: %.2f kWh",
                name, powered ? "ON" : "OFF", getEnergyUsageKwh());
    }
}
