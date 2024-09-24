package com.globallogic.eval.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Entradas de un ErrorResponse.
 */
@Getter
@Setter
public class ErrorDescription {

	private LocalDateTime timestamp;
	private Integer codigo;
	private String detail;
	
}
