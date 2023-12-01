package com.spotit.backend.testUtils;

public interface GeneralUtils {

    static String getEntityNotFoundMessage(Object id) {
        return "Entity with ID '" + id + "' not found!";
    }

    static String getErrorCreatingEntityMessage() {
        return "Error when creating new entity!";
    }

    static String getDeletedUserMessage(String userAuth0Id) {
        return "User with auth0Id '" + userAuth0Id + "' deleted.";
    }

    static String getDeletedEntityMessage(int id) {
        return "Entity with ID '" + id + "' deleted.";
    }
}
