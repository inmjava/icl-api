package com.copel.icl;
public class UserNotAuthorizedException extends AppException {

	private static final long serialVersionUID = 1L;

	public UserNotAuthorizedException(String arg0) {
		super(arg0);
	}

	public UserNotAuthorizedException(Throwable arg0) {
		super(arg0);
	}

	public UserNotAuthorizedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
}
