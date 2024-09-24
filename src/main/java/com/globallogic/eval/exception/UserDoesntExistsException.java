package com.globallogic.eval.exception;

/**
 * El usuario no existe en la base de datos, o el token es inválido, o dicho
 * token ha expirado.
 */
public class UserDoesntExistsException extends Exception {

	private static final long serialVersionUID = 8748759029268603630L;

	public UserDoesntExistsException() {
		super("El usuario no se encuentra registrado en la base de datos, "
				+ "o el token pasado es inválido o ha expirado");
	}
}
