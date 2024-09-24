package com.globallogic.eval.exception;

/**
 * Se utiliza para excepciones de validación en distintos atributos del
 * request de signup.
 */
public class InvalidFieldException extends Exception {

	private static final long serialVersionUID = -7353416555586432095L;

	public InvalidFieldException(String message) {
		super(message);
	}
}
