package org.group4;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

class Owner {
    private final String id;
    private final LocalDate startDate;
    private final List<License> licenses;

    public Owner(LocalDate startDate, List<License> licenses, String uniqueId) {
        this.id = (uniqueId == null) ? UUID.randomUUID().toString() : uniqueId;
        this.startDate = startDate;
        this.licenses = licenses;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void addLicense(License license) {
        this.licenses.add(license);
    }

    public String getId() {
        return id;
    }
}
