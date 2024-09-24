package com.globallogic.eval;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.globallogic.eval.entity.Phone;
import com.globallogic.eval.entity.User;
import com.globallogic.eval.exception.InvalidFieldException;
import com.globallogic.eval.exception.UserAlreadyExistsException;
import com.globallogic.eval.exception.UserDoesntExistsException;
import com.globallogic.eval.repository.UserRepository;
import com.globallogic.eval.request.SignUpRequest;
import com.globallogic.eval.service.UserService;

@SpringBootTest
class UserServiceTest {

	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	void testLoginValido() {
		try {
			// En el caso de hacer una base in memory puede ser que esta 
			// configuracion no funcione, habría que configurar el test para
			// H2 o mockear, por lo que se configura H2 persistido en file 
			// (lo cual permite solucionar el problema, eventualmente sin el 
			// servicio levantado o mockeando o reconfigurando; pretende ser 
			// un test simple, no es production ready si se quiere tener la 
			// base in memory, no serviría para este caso).
			List<User> users = userRepository.findAll();
			if (!users.isEmpty()) {
				String token = users.get(0).getToken();
				User user = userService.login(token);
				assertNotEquals(user.getToken(), token);
			} else {
				fail("base de datos persistida esta vacia");
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	void testLoginInvalido01() {
		try {
			userService.login("???");
			fail("Con un token inválido debe fallar, no existe el usuario");
		} catch (UserDoesntExistsException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	void testLoginInvalido02() {
		try {
			// Token expirado hardcodeado. Sirve solo si la base H2 esta 
			// configurada para ser persistida en archivo (ver test anterior).
			String token = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJjY2EwNzgwZC1mZDQ4LTRiMGItYWJjZS1kZDdkNDY1MGJkYmUiLCJpYXQiOjE3MjcxNTA5NDAsImV4cCI6MTcyNzU4Mjk0MH0.";
			userService.login(token);
			fail("Con un token expirado o inválido debe fallar");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	void testLoginTokenVacio() {
		try {
			String token = "";
			userService.login(token);
			fail("Con un token vacío debe fallar");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	void testSignupValido() {
		try {
			String userName = RandomStringUtils.randomAlphabetic(10);
			Phone phone = new Phone();
			phone.setNumber(1234567890L);
			phone.setCitycode(11);
			phone.setCountrycode("54");
			ArrayList<Phone> phones = new ArrayList<Phone>();
			phones.add(phone);
			SignUpRequest request = new SignUpRequest();
			request.setEmail(userName + "@abc.com");
			request.setName(userName);
			request.setPassword("a2asfGfdfdf4");
			request.setPhones(phones);
			User user = userService.signup(request);
			assertTrue(user.getToken().length() > 0);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	void testSignupUsuarioYaExiste() {
		try {
			String userName = RandomStringUtils.randomAlphabetic(10);
			Phone phone = new Phone();
			phone.setNumber(1234567890L);
			phone.setCitycode(11);
			phone.setCountrycode("54");
			ArrayList<Phone> phones = new ArrayList<Phone>();
			phones.add(phone);
			SignUpRequest request = new SignUpRequest();
			request.setEmail(userName + "@abc.com");
			request.setName(userName);
			request.setPassword("a2asfGfdfdf4");
			request.setPhones(phones);
			userService.signup(request);
			userService.signup(request);
			fail("Usuario definido dos veces, debería fallar");
		} catch (UserAlreadyExistsException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	void testSignupPasswordInvalida() {
		try {
			String userName = RandomStringUtils.randomAlphabetic(10);
			Phone phone = new Phone();
			phone.setNumber(1234567890L);
			phone.setCitycode(11);
			phone.setCountrycode("54");
			ArrayList<Phone> phones = new ArrayList<Phone>();
			phones.add(phone);
			SignUpRequest request = new SignUpRequest();
			request.setEmail(userName + "@abc.com");
			request.setName(userName);
			request.setPassword("a2asffdfdf4");
			request.setPhones(phones);
			userService.signup(request);
			fail("Password inválida, debería fallar");
		} catch (InvalidFieldException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	void testSignupPasswordVacia() {
		try {
			String userName = RandomStringUtils.randomAlphabetic(10);
			Phone phone = new Phone();
			phone.setNumber(1234567890L);
			phone.setCitycode(11);
			phone.setCountrycode("54");
			ArrayList<Phone> phones = new ArrayList<Phone>();
			phones.add(phone);
			SignUpRequest request = new SignUpRequest();
			request.setEmail(userName + "@abc.com");
			request.setName(userName);
			request.setPassword("");
			request.setPhones(phones);
			userService.signup(request);
			fail("Password vacía, debería fallar");
		} catch (InvalidFieldException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	void testSignupEmailInvalido() {
		try {
			String userName = RandomStringUtils.randomAlphabetic(10);
			Phone phone = new Phone();
			phone.setNumber(1234567890L);
			phone.setCitycode(11);
			phone.setCountrycode("54");
			ArrayList<Phone> phones = new ArrayList<Phone>();
			phones.add(phone);
			SignUpRequest request = new SignUpRequest();
			request.setEmail(userName + "@abc.");
			request.setName(userName);
			request.setPassword("a2asfGfdfdf4");
			request.setPhones(phones);
			userService.signup(request);
			fail("Email inválido, debería fallar");
		} catch (InvalidFieldException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	void testSignupEmailVacio() {
		try {
			String userName = RandomStringUtils.randomAlphabetic(10);
			Phone phone = new Phone();
			phone.setNumber(1234567890L);
			phone.setCitycode(11);
			phone.setCountrycode("54");
			ArrayList<Phone> phones = new ArrayList<Phone>();
			phones.add(phone);
			SignUpRequest request = new SignUpRequest();
			request.setEmail("");
			request.setName(userName);
			request.setPassword("a2asfGfdfdf4");
			request.setPhones(phones);
			userService.signup(request);
			fail("Email vacío, debería fallar");
		} catch (InvalidFieldException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
