package com.globallogic.eval.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.h2.util.StringUtils;

import com.globallogic.eval.response.ErrorResponse;

import io.jsonwebtoken.Jwts;

import com.globallogic.eval.response.ErrorDescription;

/**
 * Funciones comunes a los servicios, se intenta que sean funciones auxiliares
 * genéricas e independientes de los servicios (utilities).
 */	
public final class UserUtils {

	private final static String CIPHER_KEY = "zaqwsxcde1234567890";
	
	/**
	 * Convierte la primer letra de un texto a mayúscula. Se utiliza en las
	 * funciones de formato de fechas, ya que dependiendo del locale el mes
	 * puede empezar con una letra minúscula o mayúscula (por ejemplo, puede
	 * salir "Sep" o "sep" dependiendo del locale, independientemente del
	 * formato aplicado en DateTimeFormatter). Con esto me garantizo que las
	 * fechas siempre tienen el formato solicitado.
	 */	
	public static String capitalizeFirstLetter(String str) {
		return StringUtils.isNullOrEmpty(str) ?
				str : str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * Formatea la fecha/hora a "MMM dd, yyyy hh:mm:ss a". Por ejemplo,
	 * "Sep 23, 2024 09:03:57 PM".
	 */	
	public static String formatDateTime(LocalDateTime local) {
		DateTimeFormatter dateTimeFormatter = 
				DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm:ss a");
		return capitalizeFirstLetter(local.format(dateTimeFormatter));
	}

	/**
	 * Similar a la función anterior, pero para LocalDateTime.now(), esto es,
	 * sin parametros da la fecha/hora actual.
	 */	
	public static String formatDateTime() {
		return formatDateTime(LocalDateTime.now());
	}
	
	/**
	 * Construcción del JSON de error genérico, tanto para los errores de
	 * los endpoints "login" y "sign-up", independientemente de la excepción
	 * que genera el error.
	 */	
	public static ErrorResponse userErrorResponse(Integer codigo, String message) {
    	ErrorDescription errorDescription = new ErrorDescription();
    	errorDescription.setTimestamp(LocalDateTime.now());
    	errorDescription.setCodigo(codigo);
    	errorDescription.setDetail(message);
		return new ErrorResponse(errorDescription);
	}

	/**
	 * Genera un token JWT para realizar el login del usuario.
	 */	
    public static String generateToken(String subject) {
    	long expires = 5 * 24 * 60 * 60 * 1000;
		return Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expires))
				.compact();
    }
    
	/**
	 * Encripción simétrica DES. Se necesita este tipo de encripción para 
	 * almacenar passwords en la base de datos, ya que luego necesitan 
	 * desencriptarse para mostrarlas en el payload de las APIs.
	 */	
	public static String encryptText(String text)
			throws InvalidKeyException, NoSuchAlgorithmException, 
					InvalidKeySpecException, NoSuchPaddingException, 
					IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = CIPHER_KEY.getBytes(StandardCharsets.UTF_8);
        DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }
    
	/**
	 * Desencripción simétrica DES.
	 */	
    public static String decryptText(String text)
			throws InvalidKeyException, NoSuchAlgorithmException, 
			InvalidKeySpecException, NoSuchPaddingException, 
			IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = CIPHER_KEY.getBytes(StandardCharsets.UTF_8);
        DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedData = Base64.getDecoder().decode(text);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
    
	/**
	 * Valida el formato del email según expresión regular.
	 */	
    public static boolean isValidEmail(String email) {
    	return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$");
    }
    
	/**
	 * Valida el formato de la password según expresión regular. Debe tener 
	 * solo una Mayúscula y solamente dos números (no necesariamente 
	 * consecutivos), en combinación de letras minúsculas, largo máximo de 12 
	 * y mínimo 8. 
	 */	
    public static boolean isValidPassword(String password) {
    	return password.matches("^(?=(?:[^\\d]*\\d[^\\d]*){2}$)(?=(?:[^A-Z]*[A-Z][^A-Z]*){1}$)[a-zA-Z\\d]{8,12}$");
    }
}
