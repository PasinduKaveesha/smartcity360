package lk.jiat.smartcity360.streetlight;

/**
 * Data transfer object showing Flyweight pattern memory optimization metrics.
 */
public class StreetlightStatsDto {

    private long totalLights;
    private int uniqueModelsInRegistry;
    private long registryRequests;
    private long sharedMemoryBytes;
    private long unsharedMemoryBytes;
    private long bytesSaved;
    private double savingsPercent;

    public StreetlightStatsDto() {
    }

    public long getTotalLights() {
        return totalLights;
    }

    public void setTotalLights(long totalLights) {
        this.totalLights = totalLights;
    }

    public int getUniqueModelsInRegistry() {
        return uniqueModelsInRegistry;
    }

    public void setUniqueModelsInRegistry(int uniqueModelsInRegistry) {
        this.uniqueModelsInRegistry = uniqueModelsInRegistry;
    }

    public long getRegistryRequests() {
        return registryRequests;
    }

    public void setRegistryRequests(long registryRequests) {
        this.registryRequests = registryRequests;
    }

    public long getSharedMemoryBytes() {
        return sharedMemoryBytes;
    }

    public void setSharedMemoryBytes(long sharedMemoryBytes) {
        this.sharedMemoryBytes = sharedMemoryBytes;
    }

    public long getUnsharedMemoryBytes() {
        return unsharedMemoryBytes;
    }

    public void setUnsharedMemoryBytes(long unsharedMemoryBytes) {
        this.unsharedMemoryBytes = unsharedMemoryBytes;
    }

    public long getBytesSaved() {
        return bytesSaved;
    }

    public void setBytesSaved(long bytesSaved) {
        this.bytesSaved = bytesSaved;
    }

    public double getSavingsPercent() {
        return savingsPercent;
    }

    public void setSavingsPercent(double savingsPercent) {
        this.savingsPercent = savingsPercent;
    }
}
