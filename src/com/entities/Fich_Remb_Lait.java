package com.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Component;

@Component
@Entity
public class Fich_Remb_Lait {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_client")
	private Client client;
	private Date date_operation;
	private Date date_paiement;
	private String ref_paiement;
	private Double montant;
	private Date date_debut;/** date debut concerné /debut periode **/
	private Date date_fin;  /** date fin du concerné /fin periode **/
	private String mois;
	
	
	public Fich_Remb_Lait() {
		
	}
	

	



	public Fich_Remb_Lait(Integer id, Date date_operation, Date date_paiement, String ref_paiement, Double montant,
			Date date_debut, Date date_fin, String mois) {
		super();
		this.id = id;
		this.date_operation = date_operation;
		this.date_paiement = date_paiement;
		this.ref_paiement = ref_paiement;
		this.montant = montant;
		this.date_debut = date_debut;
		this.date_fin = date_fin;
		this.mois = mois;
	}






	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public Date getDate_operation() {
		return date_operation;
	}

	public void setDate_operation(Date date_operation) {
		this.date_operation = date_operation;
	}

	public Date getDate_paiement() {
		return date_paiement;
	}

	public void setDate_paiement(Date date_paiement) {
		this.date_paiement = date_paiement;
	}


	public Date getDate_debut() {
		return date_debut;
	}

	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}

	public Date getDate_fin() {
		return date_fin;
	}

	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}



	public String getMois() {
		return mois;
	}



	public void setMois(String mois) {
		this.mois = mois;
	}





	public String getRef_paiement() {
		return ref_paiement;
	}





	public void setRef_paiement(String ref_paiement) {
		this.ref_paiement = ref_paiement;
	}





	public Double getMontant() {
		return montant;
	}





	public void setMontant(Double montant) {
		this.montant = montant;
	}






	public Client getClient() {
		return client;
	}






	public void setClient(Client client) {
		this.client = client;
	}
	
	
	
	
	
}
