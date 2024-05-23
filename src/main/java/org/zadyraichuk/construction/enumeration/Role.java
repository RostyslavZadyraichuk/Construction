package org.zadyraichuk.construction.enumeration;

import java.io.Serializable;

public enum Role implements Serializable {
    COMPANY_OWNER,
    GENERAL_CONTRACTOR,
    WORK_SAFETY_ENGINEER,
    ADMIN;

    public String getRole() {
        return this.name();
    }
}
