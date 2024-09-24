package com.globallogic.eval.request;

import java.util.ArrayList;
import java.util.List;

import com.globallogic.eval.entity.Phone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

	private String name;
	private String email;
	private String password;
	private List<Phone> phones;
	
	public SignUpRequest() {
		this.phones = new ArrayList<Phone>();
	}
	
	public void addPhone(Phone phone) {
		this.phones.add(phone);
	}
}
