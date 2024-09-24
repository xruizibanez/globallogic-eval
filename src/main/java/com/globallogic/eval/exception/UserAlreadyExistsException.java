package com.globallogic.eval.exception;

/**
 * Se utiliza para indicar que el usuario ya se encuentra definido en la base
 * de datos (esto es, ya existe el email en la base).
 */
public class UserAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -4247842901097800494L;

	public UserAlreadyExistsException() {
		super("El usuario ya existe en la base de datos, "
				+ "su email ya se encuentra registrado");
	}
}
