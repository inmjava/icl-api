package com.copel.icl.excecao;

public class DomainException extends RuntimeException {

	private static final long serialVersionUID = -7491048007709252453L;

	public DomainException(String message) {
		super(message);
	}
}
