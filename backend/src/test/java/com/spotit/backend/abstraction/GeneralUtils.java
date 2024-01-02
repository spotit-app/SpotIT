package com.spotit.backend.abstraction;

import java.util.Map;

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

    static Map<String, Integer> getDeletedEntityResponse(int id) {
        return Map.of("id", id);
    }

    static String getInvalidUserMessage() {
        return "User account lacks necessary data!";
    }
}
