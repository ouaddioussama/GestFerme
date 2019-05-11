package com.entities;

import javax.persistence.Entity;

import org.springframework.stereotype.Component;

@Entity
@Component
public class Fournisseur extends Person {
	
	public Fournisseur() {
		
	}
	
	private String  ice;

	/**
	 * @return the ice
	 */
	public String getIce() {
		return ice;
	}

	/**
	 * @param ice the ice to set
	 */
	public void setIce(String ice) {
		this.ice = ice;
	}

	

}
