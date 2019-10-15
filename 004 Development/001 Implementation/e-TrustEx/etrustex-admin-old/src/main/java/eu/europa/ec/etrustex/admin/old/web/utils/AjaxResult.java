package eu.europa.ec.etrustex.admin.old.web.utils;

public class AjaxResult {
    private Boolean success;
    private String message;
    private Long id;


    public AjaxResult() {
    }

    public AjaxResult(Boolean success, String message, Long id) {
        this.success = success;
        this.message = message;
        this.id = id;
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
