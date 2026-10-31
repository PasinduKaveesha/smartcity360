package lk.jiat.smartcity360.zone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests verifying the Composite pattern implementation for hierarchical city zones.
 */
class ZoneTest {

    private Zone city;
    private Zone northDistrict;
    private Zone southDistrict;
    private Zone mainStreet;
    private Zone oakStreet;
    private Building b1;
    private Building b2;
    private Building b3;

    @BeforeEach
    void setUp() {
        city = new Zone("Smart City", ZoneLevel.CITY);
        northDistrict = new Zone("North District", ZoneLevel.DISTRICT);
        southDistrict = new Zone("South District", ZoneLevel.DISTRICT);

        mainStreet = new Zone("Main Street", ZoneLevel.STREET);
        oakStreet = new Zone("Oak Street", ZoneLevel.STREET);

        b1 = new Building("Building A1", 100.0, true);
        b2 = new Building("Building A2", 150.0, true);
        b3 = new Building("Building B1", 200.0, true);

        mainStreet.add(b1);
        mainStreet.add(b2);
        oakStreet.add(b3);

        northDistrict.add(mainStreet);
        southDistrict.add(oakStreet);

        city.add(northDistrict);
        city.add(southDistrict);
    }

    @Test
    @DisplayName("Verify aggregated energy usage totals recursively at every hierarchy level")
    void testUsageTotalsAtEveryLevel() {
        assertEquals(250.0, mainStreet.getEnergyUsageKwh(), 0.001);
        assertEquals(250.0, northDistrict.getEnergyUsageKwh(), 0.001);
        assertEquals(200.0, oakStreet.getEnergyUsageKwh(), 0.001);
        assertEquals(200.0, southDistrict.getEnergyUsageKwh(), 0.001);
        assertEquals(450.0, city.getEnergyUsageKwh(), 0.001);
    }

    @Test
    @DisplayName("Shutdown at a district level zeroes only its specific subtree usage")
    void testShutdownDistrictSubtreeOnly() {
        northDistrict.shutdownPower();

        assertFalse(northDistrict.isPowered());
        assertFalse(mainStreet.isPowered());
        assertFalse(b1.isPowered());
        assertFalse(b2.isPowered());
        assertEquals(0.0, northDistrict.getEnergyUsageKwh(), 0.001);

        // South district remains fully powered and unaffected
        assertTrue(southDistrict.isPowered());
        assertTrue(oakStreet.isPowered());
        assertTrue(b3.isPowered());
        assertEquals(200.0, southDistrict.getEnergyUsageKwh(), 0.001);

        // City level reflects remaining powered subtree
        assertTrue(city.isPowered());
        assertEquals(200.0, city.getEnergyUsageKwh(), 0.001);
    }

    @Test
    @DisplayName("Restore power re-enables usage calculation across entire subtree")
    void testRestorePower() {
        city.shutdownPower();
        assertFalse(city.isPowered());
        assertEquals(0.0, city.getEnergyUsageKwh(), 0.001);

        city.restorePower();
        assertTrue(city.isPowered());
        assertTrue(b1.isPowered());
        assertTrue(b2.isPowered());
        assertTrue(b3.isPowered());
        assertEquals(450.0, city.getEnergyUsageKwh(), 0.001);
    }

    @Test
    @DisplayName("Report usage produces formatted recursive subtree hierarchy text")
    void testReportUsage() {
        String report = city.reportUsage();
        assertTrue(report.contains("Smart City [CITY]"));
        assertTrue(report.contains("North District [DISTRICT]"));
        assertTrue(report.contains("Building A1 [BUILDING]"));
    }
}
