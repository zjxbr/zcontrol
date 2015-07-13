package com.jarvis.zcontrol.exception;

public class FailedExecuteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7055273588851900581L;

	public FailedExecuteException(Throwable cause) {
		super(cause);
	}
	
	public FailedExecuteException(String msg){
		super(msg);
	}
}
