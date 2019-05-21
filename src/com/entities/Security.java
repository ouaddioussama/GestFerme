package com.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Entity
@Component
public class Security {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;	
	private String mac_adresse;
	private String active;
	private Date date_debut;
	private Date date_fin;
	private String nom_client;
	/**
	 * @return the mac_adresse
	 */
	
	public  Security() {
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	public String getMac_adresse() {
		return mac_adresse;
	}
	/**
	 * @param mac_adresse the mac_adresse to set
	 */
	public void setMac_adresse(String mac_adresse) {
		this.mac_adresse = mac_adresse;
	}
	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}
	/**
	 * @return the date_debut
	 */
	public Date getDate_debut() {
		return date_debut;
	}
	/**
	 * @param date_debut the date_debut to set
	 */
	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}
	/**
	 * @return the date_fin
	 */
	public Date getDate_fin() {
		return date_fin;
	}
	/**
	 * @param date_fin the date_fin to set
	 */
	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}
	public String getNom_client() {
		return nom_client;
	}
	public void setNom_client(String nom_client) {
		this.nom_client = nom_client;
	}


}
