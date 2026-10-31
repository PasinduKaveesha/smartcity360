package lk.jiat.smartcity360.zone;

/**
 * Data transfer object holding generated hierarchical zone energy reports.
 */
public class ZoneReportDto {

    private Long zoneId;
    private String zoneName;
    private String report;

    public ZoneReportDto() {
    }

    public ZoneReportDto(Long zoneId, String zoneName, String report) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.report = report;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
