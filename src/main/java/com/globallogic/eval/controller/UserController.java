package com.globallogic.eval.controller;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.globallogic.eval.entity.User;
import com.globallogic.eval.exception.InvalidFieldException;
import com.globallogic.eval.exception.UserAlreadyExistsException;
import com.globallogic.eval.exception.UserDoesntExistsException;
import com.globallogic.eval.request.SignUpRequest;
import com.globallogic.eval.response.ErrorResponse;
import com.globallogic.eval.service.UserService;
import com.globallogic.eval.util.UserUtils;

/**
 * Los métodos signup y login sólo invocan a la capa de servicios, estando los
 * casos de excepción manejados por distintos @ExceptionHandler, uno para cada
 * tipo de excepción, y retornando cada uno de ellos un mismo formato genérico 
 * de JSON de error definido en la clase ErrorResponse según la especificación 
 * solicitada; la función común UserUtils.userErrorResponse se encarga de 
 * armarla.
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
    @PostMapping(value = "/sign-up", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public User signup(@RequestBody SignUpRequest request) throws Exception {
    	return userService.signup(request);
    }
	
    @GetMapping(value = "/login", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public User login(HttpServletRequest request) throws Exception {
    	// throw new UserAlreadyExistsException();
    	return userService.login(request.getHeader("Authorization"));
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse userException(UserAlreadyExistsException e) {
    	return UserUtils.userErrorResponse(
    			HttpStatus.CONFLICT.value(),
    			e.getMessage());
    }

    @ExceptionHandler(value = UserDoesntExistsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse userException(UserDoesntExistsException e) {
    	return UserUtils.userErrorResponse(
    			HttpStatus.UNAUTHORIZED.value(),
    			e.getMessage());
    }

    @ExceptionHandler(value = AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse userException(AuthException e) {
    	return UserUtils.userErrorResponse(
    			HttpStatus.UNAUTHORIZED.value(),
    			e.getMessage());
    }

    @ExceptionHandler(value = InvalidFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userException(InvalidFieldException e) {
    	return UserUtils.userErrorResponse(
    			HttpStatus.BAD_REQUEST.value(),
    			e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse userException(Exception e) {
    	return UserUtils.userErrorResponse(
    			HttpStatus.INTERNAL_SERVER_ERROR.value(),
    			e.getMessage());
    }
}
