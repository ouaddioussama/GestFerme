package com.entities;

import javax.persistence.Entity;

import org.springframework.stereotype.Component;

@Entity
@Component
public class Client  extends Person {
	
	private String  rib;
	private String  ice;
	
	
	public Client() {}
	
	public Client(String rib, String ice) {
		super();
		this.rib = rib;
		this.ice = ice;
	}
	/**
	 * @return the rib
	 */
	public String getRib() {
		return rib;
	}
	/**
	 * @param rib the rib to set
	 */
	public void setRib(String rib) {
		this.rib = rib;
	}
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
