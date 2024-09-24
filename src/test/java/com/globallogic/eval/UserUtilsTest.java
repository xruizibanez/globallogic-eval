package com.globallogic.eval;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.globallogic.eval.response.ErrorResponse;
import com.globallogic.eval.util.UserUtils;

class UserUtilsTest {

	@Test
	void testEncryption() {
		try {
			String plain = "abc12345";
			String encrypted = UserUtils.encryptText(plain);
			String decrypted = UserUtils.decryptText(encrypted);
			assertEquals(plain, decrypted);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testValidEmail() {
		assertTrue(UserUtils.isValidEmail("xavier@abc.com"));
	}

	@Test
	void testInvalidEmail01() {
		assertFalse(UserUtils.isValidEmail("@abc.com"));
	}

	@Test
	void testInvalidEmail02() {
		assertFalse(UserUtils.isValidEmail("xavier@.com"));
	}

	@Test
	void testInvalidEmail03() {
		assertFalse(UserUtils.isValidEmail("xavier@abc"));
	}

	@Test
	void testInvalidEmail04() {
		assertFalse(UserUtils.isValidEmail("xavier@abc."));
	}

	@Test
	void testInvalidEmail06() {
		assertFalse(UserUtils.isValidEmail("xavier@"));
	}

	@Test
	void testValidPassword01() {
		assertTrue(UserUtils.isValidPassword("a2asfGfdfdf4"));
	}

	@Test
	void testValidPassword02() {
		assertTrue(UserUtils.isValidPassword("a2Gfdfd4"));
	}

	@Test
	void testInvalidPassword01() {
		assertFalse(UserUtils.isValidPassword("a2asfGfdTfdf4"));
	}

	@Test
	void testInvalidPassword02() {
		assertFalse(UserUtils.isValidPassword("a2asffdfdf4"));
	}

	@Test
	void testInvalidPassword03() {
		assertFalse(UserUtils.isValidPassword("a2as3fGfdfdf4"));
	}

	@Test
	void testInvalidPassword04() {
		assertFalse(UserUtils.isValidPassword("a22asfGfdfdf4"));
	}

	@Test
	void testInvalidPassword05() {
		assertFalse(UserUtils.isValidPassword("a2Gf4"));
	}

	@Test
	void testInvalidPassword06() {
		assertFalse(UserUtils.isValidPassword("aaaaaaaaaaaaaaa2asfGfdfdf4"));
	}

	@Test
	void testInvalidPassword07() {
		assertFalse(UserUtils.isValidPassword("aasfGfdfdf4"));
	}

	@Test
	void testInvalidPassword08() {
		assertFalse(UserUtils.isValidPassword("aasfGfdfdf"));
	}
	
	@Test
	void testErrorResponse() {
		String mensaje = "mensaje de prueba";
		ErrorResponse error = new ErrorResponse();
		error = UserUtils.userErrorResponse(200, mensaje);
		assertEquals(mensaje, error.getError().get(0).getDetail());
	}
}
