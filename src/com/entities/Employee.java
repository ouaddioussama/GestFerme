package com.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.stereotype.Component;

import com.Enum.Profil;


@Component
@Entity
public class Employee extends Person {
	
	private String pass_word;
	
	
	@Enumerated(EnumType.STRING)
	private Profil profil;
	
	
	public Employee() {}
	


	public Employee(String pass_word, Profil profil) {
		super();
		this.pass_word = pass_word;
		this.profil = profil;
	}


	public String getPass_word() {
		return pass_word;
	}

	public void setPass_word(String pass_word) {
		this.pass_word = pass_word;
	}

	public Profil getProfil() {
		return profil;
	}


	public void setProfil(Profil profil) {
		this.profil = profil;
	}
	
	
	
	

}
