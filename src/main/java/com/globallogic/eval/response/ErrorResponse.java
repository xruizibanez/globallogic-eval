package com.globallogic.eval.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Respuesta genérica a distintas excepciones que pueden darse para ambas APIs
 * de login y sign-up. Cualquier excepción que ocurra en ambas APIs sera 
 * manejada por si @ExceptionHandler correspondiente, pero siempre retornará
 * dicho exception handler una entidad de ErrorResponse genérica, que será 
 * completada por la función de utility en UserUtils.
 */
@Getter
@Setter
public class ErrorResponse {

	private List<ErrorDescription> error;
	
	public ErrorResponse() {
		this.error = new ArrayList<ErrorDescription>();
	}
	
	public ErrorResponse(ErrorDescription errorDescription) {
		this.error = Collections.singletonList(errorDescription);
	}
}
