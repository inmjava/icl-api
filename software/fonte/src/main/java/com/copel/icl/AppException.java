package com.copel.icl;
public class AppException extends Exception {

	private static final long serialVersionUID = -2195186775377866819L;

	public AppException(String arg0) {
		super(arg0);
	}

	public AppException(Throwable arg0) {
		super(arg0);

	}

	public AppException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
