package com.globallogic.eval.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globallogic.eval.entity.Phone;

/**
 * 
 */
public interface PhoneRepository extends JpaRepository<Phone, UUID> {
	
}
