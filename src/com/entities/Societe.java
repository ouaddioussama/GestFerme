package com.entities;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Entity
@Component

public class Societe {
	@Id
    @GeneratedValue (strategy=GenerationType.AUTO)   

	private Integer id_Societe;
	private String nom_Societe;
	private String code_postal;
	private String email;
	private String fax;	
	private String telephone;
	private String rib;
	private String adresse;
	
	public Societe() {
		
	}
	
	public Societe( String nom_Societe, String code_postal, String email, String fax,
			String telephone, String rib,String adresse) {
		super();
		this.nom_Societe = nom_Societe;
		this.code_postal = code_postal;
		this.email = email;
		this.fax = fax;
		this.telephone = telephone;
		this.rib = rib;
		this.adresse=adresse;
										}
	


	public Integer getId_Societe() {
		return id_Societe;
	}


	public void setId_Societe(Integer id_Societe) {
		this.id_Societe = id_Societe;
	}


	public String getNom_Societe() {
		return nom_Societe;
	}


	public void setNom_Societe(String nom_Societe) {
		this.nom_Societe = nom_Societe;
	}


	public String getcode_postal() {
		return code_postal;
	}


	public void setcode_postal(String code_postal) {
		this.code_postal = code_postal;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getfax() {
		return fax;
	}


	public void setfax(String fax) {
		this.fax = fax;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getRib() {
		return rib;
	}


	public void setRib(String rib) {
		this.rib = rib;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	@Override
	public String toString() {
		return "Societe [id_Societe=" + id_Societe + ", nom_Societe=" + nom_Societe + ", code_postal=" + code_postal
				+ ", email=" + email + ", fax=" + fax + ", telephone=" + telephone + ", rib=" + rib + ", adresse="
				+ adresse + "]";
	}
	
    
	
}

