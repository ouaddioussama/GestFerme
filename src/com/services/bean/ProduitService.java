package com.services.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Enum.Categorie;
import com.dao.interfaces.InterfProduitDao;
import com.entities.Achat;
import com.entities.Produit;

@ManagedBean(name = "produitService")
@ViewScoped
@Service

public class ProduitService extends ObjectService<Produit> implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@Autowired
	protected Produit objectToInsert;

	@Autowired
	protected InterfProduitDao dao;

	public ProduitService() {
		System.out.println("construct ProduitService");

	}

	public Produit getObjectToInsert() {
		return objectToInsert;
	}

	public void setObjectToInsert(Produit objectToInsert) {
		this.objectToInsert = objectToInsert;
	}

	public void create() throws Exception {
		System.out.println("inside create");

		if (objectToInsert != null) {
			System.out.println("inside create");
			dao.createInstance(objectToInsert);
			try {
				listObjects.add(objectToInsert);

			} catch (Exception e) {
				listObjects = (List<Produit>) dao.findAll();
			}
			Help.msg = "insere avec Succes";
			reset();

		} else {
			throw new Exception("objectToInsert can not be null");
		}
	}


  /** Les Animaux **/
	public void createProdAnimeauxAchat() throws Exception {
		objectToInsert.setCategorie(Categorie.Animeaux_Achat);
		create();
	}
	
	public void createProdAnimeauxVente() throws Exception {
		objectToInsert.setCategorie(Categorie.Animeaux_Vente);
		create();
	}
	
	  /** Agricole **/

	public void createProdAgricoleAchat() throws Exception {
		objectToInsert.setCategorie(Categorie.Agricole_Achat);
		create();
	}
	
	public void createProdAgricoleVente() throws Exception {
		objectToInsert.setCategorie(Categorie.Agricole_Vente);
		create();
	}

	  /** Les Auto **/

	public void createProdAutoAchat() throws Exception {
		objectToInsert.setCategorie(Categorie.Auto_Achat);
		create();
	}
	
	public void createProdAutoVente() throws Exception {
		objectToInsert.setCategorie(Categorie.Auto_Vente);
		create();
	}

	  /** Le lait **/
	public void createProdLait() throws Exception {
		objectToInsert.setCategorie(Categorie.Lait);
		create();
	}

	  /** Les autres **/
	public void createProdAutreAchat() throws Exception {
		objectToInsert.setCategorie(Categorie.Autre_Achat);
		create();
	}
	
	public void createProdAutreVente() throws Exception {
		objectToInsert.setCategorie(Categorie.Autre_Vente);
		create();
	}

	public void printAll() {
		System.out.println(dao.findAll().size());

	}

	@PostConstruct
	public void init() {

		path = "/views/produit/index";
		//
		listObjects = (List<Produit>) dao.findAll();
		System.out.println("init ProduitService");

		System.out.println(listObjects.size());
		//

	}

	public void reset() {
		objectToInsert = new Produit();

	}

	public void onRowEdit(RowEditEvent event) throws IOException {

		Produit editedModele = (Produit) event.getObject();

		if (editedModele != null) {
			dao.updateIstance(editedModele);
			Help.msg = "mise a jour faite avec Succes";

		} else {
			System.out.println("objectToInsert is null !");
		}

	}

	public void delete(Produit c) throws Exception {

		if (c != null) {
			dao.deleteInstance(c);
			listObjects.remove(c);
			Help.msg = "supprime avec Succes";

		} else {
			throw new Exception("objectSelected can not be null");
		}
	}

	public void tryMe() {
		System.out.println("hello!");
	}

	// Recuperer la liste des achats selon type de produit
	public List<Produit> getListProduit(Categorie c) {
		List<Produit> listprod = null;
		if (listObjects != null) {
			try {
				listprod = listObjects.stream().filter((p) ->

				p.getCategorie() == null ? null : p.getCategorie() == c

				).collect(Collectors.toList());

			} catch (Exception e) {

			}

		}

		return listprod;
	}

	public List<Produit> getListProduitAgricoleAchat() {

		return getListProduit(Categorie.Agricole_Achat);

	}
	public List<Produit> getListProduitLaitVente() {

		return getListProduit(Categorie.Lait);

	}
	
	public List<Produit> getListProduitAgricoleVente() {

		return getListProduit(Categorie.Agricole_Vente);

	}

	public List<Produit> getListProduitAnimeauxAchat() {

		return getListProduit(Categorie.Animeaux_Achat);

	}	
	

	public List<Produit> getListProduitAnimeauxVente() {

		return getListProduit(Categorie.Animeaux_Vente);

	}

	public List<Produit> getListProduitAutoAchat() {

		return getListProduit(Categorie.Auto_Achat);

	}	
	
	public List<Produit> getListProduitAutoVente() {

		return getListProduit(Categorie.Auto_Vente);

	}

	public List<Produit> getListProduitAutreAchat() {

		return getListProduit(Categorie.Autre_Achat);

	}
	public List<Produit> getListProduitAutreVente() {

		return getListProduit(Categorie.Autre_Vente);

	}
	


}
