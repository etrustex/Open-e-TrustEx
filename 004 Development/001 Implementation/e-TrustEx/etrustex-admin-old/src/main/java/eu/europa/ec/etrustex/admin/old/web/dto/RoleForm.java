package eu.europa.ec.etrustex.admin.old.web.dto;

import java.io.Serializable;

public class RoleForm implements Serializable {
    private Long id;
    private String name;
    private String code;
    private Boolean technicalFlag;
    private Boolean bidirectionalFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getTechnicalFlag() {
        return technicalFlag;
    }

    public void setTechnicalFlag(Boolean technicalFlag) {
        this.technicalFlag = technicalFlag;
    }

    public Boolean getBidirectionalFlag() {
        return bidirectionalFlag;
    }

    public void setBidirectionalFlag(Boolean bidirectionalFlag) {
        this.bidirectionalFlag = bidirectionalFlag;
    }
}
