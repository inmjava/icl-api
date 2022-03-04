package com.copel.picmicroservice.excecao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {


	private static final long serialVersionUID = 975033459286939895L;

	public BadRequestException(String message) {
		super(message);
	}
}
