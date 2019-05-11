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



@Entity
@Component
public class Fiche_Personnel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_employee")
	private Employee employee;
	private Date date_operation;
	private Date date_paiement;
	private Double salaire;
	private Date date_debut;/** date debut du travail /debut periode **/
	private Date date_fin;  /** date fin du travail /fin periode **/
	private String mois;
	
	
	public Fiche_Personnel() {
		
	}
	


	public Fiche_Personnel(Integer id, Employee employee, Date date_operation, Date date_paiement, Double salaire,
			Date date_debut, Date date_fin, String mois) {
		super();
		this.id = id;
		this.employee = employee;
		this.date_operation = date_operation;
		this.date_paiement = date_paiement;
		this.salaire = salaire;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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

	public Double getSalaire() {
		return salaire;
	}

	public void setSalaire(Double salaire) {
		this.salaire = salaire;
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
	
	
	 
	
	
}
