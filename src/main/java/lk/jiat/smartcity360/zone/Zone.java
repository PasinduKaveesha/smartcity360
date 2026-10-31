package lk.jiat.smartcity360.zone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite node representing aggregate city areas (City, District, or Street).
 */
public class Zone implements ZoneNode {

    private Long id;
    private final String name;
    private final ZoneLevel level;
    private final List<ZoneNode> children = new ArrayList<>();

    public Zone(Long id, String name, ZoneLevel level) {
        if (level == ZoneLevel.BUILDING) {
            throw new IllegalArgumentException("Composite Zone cannot have BUILDING level");
        }
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public Zone(String name, ZoneLevel level) {
        this(null, name, level);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void add(ZoneNode node) {
        if (node != null) {
            children.add(node);
        }
    }

    public void remove(ZoneNode node) {
        children.remove(node);
    }

    public List<ZoneNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ZoneLevel getLevel() {
        return level;
    }

    @Override
    public double getEnergyUsageKwh() {
        return children.stream().mapToDouble(ZoneNode::getEnergyUsageKwh).sum();
    }

    @Override
    public boolean isPowered() {
        return children.stream().anyMatch(ZoneNode::isPowered);
    }

    @Override
    public void shutdownPower() {
        for (ZoneNode child : children) {
            child.shutdownPower();
        }
    }

    @Override
    public void restorePower() {
        for (ZoneNode child : children) {
            child.restorePower();
        }
    }

    @Override
    public String reportUsage() {
        StringBuilder sb = new StringBuilder();
        appendReport(sb, 0);
        return sb.toString();
    }

    private void appendReport(StringBuilder sb, int indentLevel) {
        sb.append("  ".repeat(indentLevel))
          .append(String.format("%s [%s] - Powered: %s, Total Usage: %.2f kWh%n",
                  name, level, isPowered() ? "ON" : "OFF", getEnergyUsageKwh()));
        for (ZoneNode child : children) {
            if (child instanceof Zone compositeChild) {
                compositeChild.appendReport(sb, indentLevel + 1);
            } else {
                sb.append("  ".repeat(indentLevel + 1))
                  .append(child.reportUsage())
                  .append(System.lineSeparator());
            }
        }
    }
}
