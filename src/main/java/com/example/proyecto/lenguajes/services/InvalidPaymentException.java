package com.example.proyecto.lenguajes.services;

public class InvalidPaymentException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPaymentException(String message) {
        super(message);
    }
}
