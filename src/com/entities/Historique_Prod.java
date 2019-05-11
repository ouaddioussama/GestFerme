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

import com.Enum.Types_Operation;

/**
 * Role : cette classe permet garder un historique des operations sur les
 * produit de carburant
 * 
 * Operation : Débit / Crédit
 * 
 * @author Oussama
 *
 */
@Entity
@Component
public class Historique_Prod {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id_historique;

	private Date date_operation;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="id_produit")
	private Produit produit;

	@Enumerated(EnumType.STRING)
	private Types_Operation type_operation;

	private Double quantite_operation;

	private Double quantite_precedente;

	private Double quantite_actuel;


	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_user")
	private Employee  user_logged;


	
	public Historique_Prod() {
	}


	
	
	public Historique_Prod(Integer id_historique, Date date_operation, Produit produit, Types_Operation type_operation,
			Double quantite_operation, Double quantite_precedente, Double quantite_actuel, Employee user_logged) {
		super();
		this.id_historique = id_historique;
		this.date_operation = date_operation;
		this.produit = produit;
		this.type_operation = type_operation;
		this.quantite_operation = quantite_operation;
		this.quantite_precedente = quantite_precedente;
		this.quantite_actuel = quantite_actuel;
		this.user_logged = user_logged;
	}




	public Historique_Prod( Date date_operation, Produit produit, Types_Operation type_operation,
			Double quantite_operation, Double quantite_precedente, Double quantite_actuel) {
		super();
		this.date_operation = date_operation;
		this.produit = produit;
		this.type_operation = type_operation;
		this.quantite_operation = quantite_operation;
		this.quantite_precedente = quantite_precedente;
		this.quantite_actuel = quantite_actuel;
	}


	public Integer getId_historique() {
		return id_historique;
	}

	public void setId_historique(Integer id_historique) {
		this.id_historique = id_historique;
	}

	public Date getDate_operation() {
		return date_operation;
	}

	public void setDate_operation(Date date_operation) {
		this.date_operation = date_operation;
	}

	public Types_Operation getType_operation() {
		return type_operation;
	}

	public void setType_operation(Types_Operation type_operation) {
		this.type_operation = type_operation;
	}

	public Double getQuantite_operation() {
		return quantite_operation;
	}

	public void setQuantite_operation(Double quantite_operation) {
		this.quantite_operation = quantite_operation;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public Double getQuantite_precedente() {
		return quantite_precedente;
	}

	public void setQuantite_precedente(Double quantite_precedente) {
		this.quantite_precedente = quantite_precedente;
	}

	public Double getQuantite_actuel() {
		return quantite_actuel;
	}

	public void setQuantite_actuel(Double quantite_actuel) {
		this.quantite_actuel = quantite_actuel;
	}

	public Employee getUser_logged() {
		return user_logged;
	}

	public void setUser_logged(Employee user_logged) {
		this.user_logged = user_logged;
	};

	
}
