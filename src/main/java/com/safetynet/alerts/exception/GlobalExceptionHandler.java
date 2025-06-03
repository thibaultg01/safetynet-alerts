package com.safetynet.alerts.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Gestion centralisée des exceptions pour toute l'application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
		String message = ex.getMessage();
		logger.error("Erreur métier : {}", message);

		if (message != null && message.contains("existe déjà")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(message); // 409 Conflict
		}
		if (message != null && (message.contains("introuvable") || message.contains("aucun"))) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); // 404 Not Found
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message); // 400 fallback
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
		String paramName = ex.getParameterName();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre '" + paramName + "' est requis.");
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		logger.error("Ressource introuvable : {}", message);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		logger.error("Erreur inattendue : {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur.");
	}
}