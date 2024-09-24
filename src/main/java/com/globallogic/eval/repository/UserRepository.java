package com.globallogic.eval.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globallogic.eval.entity.User;

/**
 * Spring Data JPA, con definiciones de funciones específicas necesarias en
 * la capa de servicios (para consultas específicas por token y email).
 */
public interface UserRepository extends JpaRepository<User, UUID> {
	User findByToken(String token);
	boolean existsByToken(String token);
	boolean existsByEmail(String email);
}
