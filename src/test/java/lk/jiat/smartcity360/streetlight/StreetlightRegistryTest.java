package lk.jiat.smartcity360.streetlight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests verifying the Flyweight pattern implementation for streetlight model sharing.
 */
class StreetlightRegistryTest {

    @BeforeEach
    void setUp() {
        StreetlightModelRegistry.clear();
    }

    @Test
    @DisplayName("Same model code returns identical canonical Flyweight instance (assertSame)")
    void testSameModelReturnsSameInstance() {
        StreetlightModel model1 = StreetlightModelRegistry.getModel(
                "LED-60W-STD", "LED", 60, 6000, "LINEAR", "Philips"
        );
        StreetlightModel model2 = StreetlightModelRegistry.getModel(
                "LED-60W-STD", "LED", 60, 6000, "LINEAR", "Philips"
        );

        assertSame(model1, model2, "Registry must return identical object reference for identical modelCode");
    }

    @Test
    @DisplayName("Registry size counts unique shared model instances only")
    void testSizeCountsUniqueModelsOnly() {
        StreetlightModelRegistry.getModel("LED-60W-STD", "LED", 60, 6000, "LINEAR", "Philips");
        StreetlightModelRegistry.getModel("LED-60W-STD", "LED", 60, 6000, "LINEAR", "Philips");
        StreetlightModelRegistry.getModel("LED-90W-HIGH", "LED", 90, 9500, "STEPPED", "Osram");
        StreetlightModelRegistry.getModel("LED-90W-HIGH", "LED", 90, 9500, "STEPPED", "Osram");
        StreetlightModelRegistry.getModel("SOLAR-40W-ECO", "SOLAR", 40, 4000, "LINEAR", "SunPower");

        assertEquals(3, StreetlightModelRegistry.size(), "Registry size should count unique models regardless of invocation count");
        assertEquals(5, StreetlightModelRegistry.getRequestCount(), "Request counter should track total lookups");
    }

    @Test
    @DisplayName("Multiple SmartStreetlight fixtures share identical intrinsic Flyweight reference")
    void testStreetlightsShareModelReference() {
        StreetlightModel sharedModel = StreetlightModelRegistry.getModel(
                "SMART-75W-IOT", "SMART_LED", 75, 8000, "ADAPTIVE", "Signify"
        );

        SmartStreetlight light1 = new SmartStreetlight(1L, 6.9271, 79.8612, 4L, sharedModel);
        SmartStreetlight light2 = new SmartStreetlight(2L, 6.9272, 79.8613, 4L, sharedModel);

        assertSame(light1.getModel(), light2.getModel(), "Both streetlight fixtures must share same model instance");
    }
}
