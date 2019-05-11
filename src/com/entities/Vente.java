package com.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Component;

import com.Enum.Types_Reglement;
import com.Enum.Unite;

@Entity
@Component
public class Vente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_client")
	private Client client;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_produit")
	private Produit produit;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_responsable")
	private Employee responsable;


	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_user")
	private Employee  user_logged;


	
	// reference d'achat
	private Integer ref_bon_achat;

	// reference d'achat
	private Integer ref_bon_vente;

	@Enumerated(EnumType.STRING)
	private Unite unite;

	private double prix_unitaire;

	private double quantite;

	private double montant_global;
	
	/** utilisé en cas du Lait **/
	private Double montant_reglement;

	private String designation_achat;

	private Date dateOperation;

	private Date dateAchat;

	@Enumerated(EnumType.STRING)
	private Types_Reglement mode_paiement;

	// date dans laquelle je dois payé si c'est chèque
	private Date dateDePaiement;

	private String ref_reglement;

	public Vente() {

	}


	

	public Vente(Integer id, Client client, Produit produit, Employee responsable, Employee user_logged,
			Integer ref_bon_achat, Integer ref_bon_vente, Unite unite, double prix_unitaire, double quantite,
			double montant_global, Double montant_reglement, String designation_achat, Date dateOperation,
			Date dateAchat, Types_Reglement mode_paiement, Date dateDePaiement, String ref_reglement) {
		super();
		this.id = id;
		this.client = client;
		this.produit = produit;
		this.responsable = responsable;
		this.user_logged = user_logged;
		this.ref_bon_achat = ref_bon_achat;
		this.ref_bon_vente = ref_bon_vente;
		this.unite = unite;
		this.prix_unitaire = prix_unitaire;
		this.quantite = quantite;
		this.montant_global = montant_global;
		this.montant_reglement = montant_reglement;
		this.designation_achat = designation_achat;
		this.dateOperation = dateOperation;
		this.dateAchat = dateAchat;
		this.mode_paiement = mode_paiement;
		this.dateDePaiement = dateDePaiement;
		this.ref_reglement = ref_reglement;
	}




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public Date getDateOperation() {
		return dateOperation;
	}

	public void setDateOperation(Date dateOperation) {
		this.dateOperation = dateOperation;
	}

	public Employee getResponsable() {
		return responsable;
	}

	public void setResponsable(Employee responsable) {
		this.responsable = responsable;
	}

	public Integer getRef_bon_achat() {
		return ref_bon_achat;
	}

	public void setRef_bon_achat(Integer ref_bon_achat) {
		this.ref_bon_achat = ref_bon_achat;
	}

	public Unite getUnite() {
		return unite;
	}

	public void setUnite(Unite unite) {
		this.unite = unite;
	}

	public double getPrix_unitaire() {
		return prix_unitaire;
	}

	public void setPrix_unitaire(double prix_unitaire) {
		this.prix_unitaire = prix_unitaire;
	}

	public double getQuantite() {
		return quantite;
	}

	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}

	public double getMontant_global() {
		return montant_global;
	}

	public void setMontant_global(double montant_global) {
		this.montant_global = montant_global;
	}

	public String getDesignation_achat() {
		return designation_achat;
	}

	public void setDesignation_achat(String designation_achat) {
		this.designation_achat = designation_achat;
	}

	public Date getDateAchat() {
		return dateAchat;
	}

	public void setDateAchat(Date dateAchat) {
		this.dateAchat = dateAchat;
	}

	public Date getDateDePaiement() {
		return dateDePaiement;
	}

	public void setDateDePaiement(Date dateDePaiement) {
		this.dateDePaiement = dateDePaiement;
	}

	public Types_Reglement getMode_paiement() {
		return mode_paiement;
	}

	public void setMode_paiement(Types_Reglement mode_paiement) {
		this.mode_paiement = mode_paiement;
	}

	public String getRef_reglement() {
		return ref_reglement;
	}

	public void setRef_reglement(String ref_reglement) {
		this.ref_reglement = ref_reglement;
	}

	public Integer getRef_bon_vente() {
		return ref_bon_vente;
	}

	public void setRef_bon_vente(Integer ref_bon_vente) {
		this.ref_bon_vente = ref_bon_vente;
	}

	public Double getMontant_reglement() {
		return montant_reglement;
	}

	public void setMontant_reglement(Double montant_reglement) {
		this.montant_reglement = montant_reglement;
	}




	public Employee getUser_logged() {
		return user_logged;
	}




	public void setUser_logged(Employee user_logged) {
		this.user_logged = user_logged;
	}

	
	
}
