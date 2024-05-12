package com.spotit.backend.domain.referenceData.applicationStatus;

public enum ApplicationStatus {
    DELIVERED("Dostarczono"),
    ACCEPTED("Zaakceptowano"),
    REJECTED("Odrzucono");

    private String name;

    private ApplicationStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
