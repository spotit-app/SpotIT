package com.spotit.backend.domain.referenceData.applicationStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ApplicationStatusTest {

    @Test
    void testEnumValues() {
        assertEquals("Dostarczono", ApplicationStatus.DELIVERED.getName());
        assertEquals("Zaakceptowano", ApplicationStatus.ACCEPTED.getName());
        assertEquals("Odrzucono", ApplicationStatus.REJECTED.getName());
    }

    @Test
    void testEnumCount() {
        ApplicationStatus[] values = ApplicationStatus.values();
        assertEquals(3, values.length);
    }
}
