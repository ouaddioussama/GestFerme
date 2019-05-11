package com.entities;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

import com.Enum.Categorie;


@Entity
@Component
public class Produit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String  ref_prod;
	private String designation_prod;
	private double quantite_actuelle;

	
	@Enumerated(EnumType.STRING)
	private Categorie categorie;
	

	


	public Integer getId() {
		return id;
	}





	public void setId(Integer id) {
		this.id = id;
	}





	public String getRef_prod() {
		return ref_prod;
	}





	public void setRef_prod(String ref_prod) {
		this.ref_prod = ref_prod;
	}





	public String getDesignation_prod() {
		return designation_prod;
	}





	public void setDesignation_prod(String designation_prod) {
		this.designation_prod = designation_prod;
	}





	public Categorie getCategorie() {
		return categorie;
	}



	
	


	public double getQuantite_actuelle() {
		return quantite_actuelle;
	}





	public void setQuantite_actuelle(double quantite_actuelle) {
		this.quantite_actuelle = quantite_actuelle;
	}





	public Produit() {
		super();
		// TODO Auto-generated constructor stub
	}

	






	public Produit(Integer id, String ref_prod, String designation_prod, Double quantite_actuelle,
			Categorie categorie) {
		super();
		this.id = id;
		this.ref_prod = ref_prod;
		this.designation_prod = designation_prod;
		this.quantite_actuelle = quantite_actuelle;
		this.categorie = categorie;
	}





	public Produit( String designation_prod, Categorie categorie) {
		this.designation_prod = designation_prod;
		this.categorie = categorie;
	}





	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}





	public boolean equals(Object other) {
        return other instanceof Produit && (id!= null) ? id.equals(((Produit) other).id) : (other == this);
    }





	

}
