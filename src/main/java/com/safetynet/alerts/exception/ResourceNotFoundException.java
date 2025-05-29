package com.safetynet.alerts.exception;

/**
 * Exception générique pour les cas où une ressource attendue n'est pas trouvée.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}