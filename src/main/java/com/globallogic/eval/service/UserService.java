package com.globallogic.eval.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.globallogic.eval.entity.User;
import com.globallogic.eval.exception.InvalidFieldException;
import com.globallogic.eval.exception.UserAlreadyExistsException;
import com.globallogic.eval.exception.UserDoesntExistsException;
import com.globallogic.eval.repository.PhoneRepository;
import com.globallogic.eval.repository.UserRepository;
import com.globallogic.eval.request.SignUpRequest;
import com.globallogic.eval.util.UserUtils;

/**
 * 
 */
@Service
public class UserService {

	private final String MSG_EMAIL_VACIO = "El email no puede ser vacío o estar faltante";
	private final String MSG_EMAIL_INVALIDO = "El email especificado posee un formato inválido";
	private final String MSG_PASS_VACIA = "La password no puede ser vacía o faltante";
	private final String MSG_PASS_INVALIDA = "La password especificada no cumple los requerimientos";
	private final String MSG_TOKEN_INVALIDO = "El token no está definido, o está vacío";
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PhoneRepository phoneRepository;
	
	/**
	 * Registro de un usuario. Valida que el email y la password tengan el 
	 * formato correcto. Luego valida que el email no exista en la base (ya que
	 * se considera que el usuario se identifica por dicho email, no el nombre
	 * que sería optativo). Genera los datos del usuario en las entidades 
	 * correspondientes, incluyendo un nuevo token JWT y encriptando la 
	 * password, y luego persiste las entidades. Retorna al método del 
	 * controlador la entidad User con la password desencriptada, estando ésta
	 * únicamente encriptada en la base de datos. Es transaccional, debe 
	 * completar las actualizaciones de las tablas USERS y PHONES en forma 
	 * atómica.
	 */
	@Transactional
	public User signup(SignUpRequest request) throws Exception {
		
		if (StringUtils.isNullOrEmpty(request.getEmail())) {
			throw new InvalidFieldException(MSG_EMAIL_VACIO);
		}
		
		if (StringUtils.isNullOrEmpty(request.getPassword())) {
			throw new InvalidFieldException(MSG_PASS_VACIA);
		}
		
		if (! UserUtils.isValidEmail(request.getEmail())) {
			throw new InvalidFieldException(MSG_EMAIL_INVALIDO);
		}
		
		if (! UserUtils.isValidPassword(request.getPassword())) {
			throw new InvalidFieldException(MSG_PASS_INVALIDA);
		}
		
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistsException();
		}
		
		UUID userid = UUID.randomUUID();
		String plainPassword = request.getPassword();

		User user = new User();
		user.setId(userid);
		user.setCreated(UserUtils.formatDateTime());
		user.setLastLogin(UserUtils.formatDateTime());
		user.setToken(UserUtils.generateToken(userid.toString()));
		user.setIsActive(true);
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(UserUtils.encryptText(plainPassword));
		
		userRepository.save(user);
		
		request.getPhones().forEach(phone -> {
			phone.setId(UUID.randomUUID());
			phone.setUserId(userid);
			phoneRepository.save(phone);
		});
		
		user.setPassword(plainPassword);
		user.setPhones(request.getPhones());
		
		return user;
	}
	
	/**
	 * Login de un usuario a partir del token pasado como parametro (que
	 * proviene de los headers en la API REST correspondiente. Valida que el
	 * token sea valido, y luego que exista en la base de datos como clave de
	 * búsqueda para el usuario. Renueva para el usuario actual en la base de
	 * datos el campo lastLogin y genera un nuevo token JWT para ser utilizado 
	 * en el próximo login del usuario en cuestión. Al retornar el usuario 
	 * al método invocador del controller, lo hace propagando una password
	 * sin encriptar (por lo tanto, la password sólo estará encriptada en la
	 * tabla USERS, mostrando las API correspondientes la password sin 
	 * encriptar).
	 */
	public User login(String token) throws Exception {
		
		if (StringUtils.isNullOrEmpty(token)) {
			throw new InvalidFieldException(MSG_TOKEN_INVALIDO);
		}
		
		if (! userRepository.existsByToken(token)) {
			throw new UserDoesntExistsException();
		}
		
		User user = userRepository.findByToken(token);
		
		user.setLastLogin(UserUtils.formatDateTime());
		user.setToken(UserUtils.generateToken(user.getId().toString()));
		
		userRepository.save(user);

		user.setPassword(UserUtils.decryptText(user.getPassword()));

		return user;
	}
}
