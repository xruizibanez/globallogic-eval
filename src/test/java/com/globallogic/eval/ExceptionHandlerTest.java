package com.globallogic.eval;

import static org.junit.jupiter.api.Assertions.*;

import javax.security.auth.message.AuthException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.globallogic.eval.controller.UserController;
import com.globallogic.eval.exception.InvalidFieldException;
import com.globallogic.eval.exception.UserAlreadyExistsException;
import com.globallogic.eval.exception.UserDoesntExistsException;
import com.globallogic.eval.response.ErrorResponse;

@SpringBootTest
class ExceptionHandlerTest {

	@Autowired
	UserController userController;
	
	@Test
	void testUserAlreadyExistsException() {
		ErrorResponse error = userController.userException(new UserAlreadyExistsException());
		assertEquals(error.getError().get(0).getCodigo(), HttpStatus.CONFLICT.value());
	}
	
	@Test
	void testUserDoesntExistsException() {
		ErrorResponse error = userController.userException(new UserDoesntExistsException());
		assertEquals(error.getError().get(0).getCodigo(), HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	void testAuthException() {
		ErrorResponse error = userController.userException(new AuthException());
		assertEquals(error.getError().get(0).getCodigo(), HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	void testInvalidFieldException() {
		ErrorResponse error = userController.userException(new InvalidFieldException("?"));
		assertEquals(error.getError().get(0).getCodigo(), HttpStatus.BAD_REQUEST.value());
	}
}
