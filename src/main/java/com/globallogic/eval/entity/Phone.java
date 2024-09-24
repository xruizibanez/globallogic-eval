package com.globallogic.eval.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * Se anota con @JsonIgnore los atributos que no deben mostrarse como response
 * para ambas APIs login y sign-up. La columna "userid" es la foreign key
 * a los registros de la tabla USERS. Se considera que la columna "number" es
 * la única que no puede ser nula, siendo citycode y countrycode opcionales en
 * el request.
 */
@Getter
@Setter
@Entity
@Table(name = "phones")
public class Phone {

	@Id
	@JsonIgnore
	private UUID id;

	@Column(name = "userid")
	@JsonIgnore
	private UUID userId;

	@Column(name = "number", nullable = false)
	private Long number;

	@Column(name = "citycode")
	private Integer citycode;

	@Column(name = "countrycode")
	private String countrycode;

}
