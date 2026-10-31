package lk.jiat.smartcity360.resident;

/**
 * Data transfer object representing a municipal staff officer.
 */
public class StaffMemberDto {

    private Long id;
    private String fullName;
    private String role;
    private String email;

    public StaffMemberDto() {
    }

    public StaffMemberDto(Long id, String fullName, String role, String email) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
