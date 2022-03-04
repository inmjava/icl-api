package com.copel.picmicroservice.excecao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -933555241287175897L;

	public NotFoundException(String message) {
		super(message);
	}
}
