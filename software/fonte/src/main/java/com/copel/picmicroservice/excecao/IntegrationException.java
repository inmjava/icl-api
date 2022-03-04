package com.copel.picmicroservice.excecao;

public class IntegrationException extends RuntimeException {

	private static final long serialVersionUID = -7491048007709252453L;

	public IntegrationException(String message) {
		super(message);
	}
}
