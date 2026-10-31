package lk.jiat.smartcity360.policy;

/**
 * Data transfer object returning syntax validation outcomes and error offset positions.
 */
public class ValidationResultDto {

    private boolean valid;
    private String message;
    private Integer errorPosition;

    public ValidationResultDto() {
    }

    public ValidationResultDto(boolean valid, String message, Integer errorPosition) {
        this.valid = valid;
        this.message = message;
        this.errorPosition = errorPosition;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getErrorPosition() {
        return errorPosition;
    }

    public void setErrorPosition(Integer errorPosition) {
        this.errorPosition = errorPosition;
    }
}
