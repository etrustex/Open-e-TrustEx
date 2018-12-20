package eu.europa.ec.etrustex.admin.old.web.utils;

import java.io.Serializable;

public class Referer implements Serializable {
    private String action = "";
    private String controller;

    public String getAction() {
        return action;
    }

    public void setAction(String value) {
        this.action = value;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }
}
