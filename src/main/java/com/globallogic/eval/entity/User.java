package com.globallogic.eval.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * La tabla USERS tendrá una asociación one-to-many con la tabla PHONES, donde
 * la foreign key será la columna USERID en la segunda de dichas tablas. Se
 * crean dos índices adicionales para que las operaciones de consulta por 
 * email y por token sean más eficientes (ambos índices deben ser con clave
 * única, estas columnas no pueden admitir duplicados).
 */
@Getter
@Setter
@Entity
@Table(name = "users",
		indexes = {
				@Index(name = "idxemail", columnList = "email", unique = true),
				@Index(name = "idxtoken", columnList = "token", unique = true)})
public class User {

	@Id
	private UUID id;

	@Column(name = "created", nullable = false)
	private String created;

	@Column(name = "lastlogin", nullable = false)
	private String lastLogin;

	@Column(name = "token", nullable = false)
	private String token;

	@Column(name = "isactive", nullable = false)
	private Boolean isActive;

	@Column(name = "name")
	private String name;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "userid")
	private List<Phone> phones;
	
}
