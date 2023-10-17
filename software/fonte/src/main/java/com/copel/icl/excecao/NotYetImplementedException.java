package com.copel.icl.excecao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
public class NotYetImplementedException extends RuntimeException {
	
	private static final long serialVersionUID = -6306911336172769107L;

	public NotYetImplementedException(String message) {
		super(message);
	}
}
