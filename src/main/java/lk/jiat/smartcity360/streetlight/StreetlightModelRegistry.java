package lk.jiat.smartcity360.streetlight;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * Flyweight factory and registry managing unique shared StreetlightModel instances.
 */
public final class StreetlightModelRegistry {

    private static final ConcurrentHashMap<String, StreetlightModel> REGISTRY = new ConcurrentHashMap<>();
    private static final AtomicLong REQUEST_COUNTER = new AtomicLong(0);

    private StreetlightModelRegistry() {
    }

    public static StreetlightModel getModel(String modelCode, Function<String, StreetlightModel> creator) {
        REQUEST_COUNTER.incrementAndGet();
        return REGISTRY.computeIfAbsent(modelCode, creator);
    }

    public static StreetlightModel getModel(String modelCode, String lampType, int wattage, int maxLumens, String dimProfile, String manufacturer) {
        return getModel(modelCode, code -> new StreetlightModel(code, lampType, wattage, maxLumens, dimProfile, manufacturer));
    }

    public static StreetlightModel getModel(StreetlightModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }
        return getModel(model.getModelCode(), code -> model);
    }

    public static int size() {
        return REGISTRY.size();
    }

    public static long getRequestCount() {
        return REQUEST_COUNTER.get();
    }

    public static void clear() {
        REGISTRY.clear();
        REQUEST_COUNTER.set(0);
    }
}
