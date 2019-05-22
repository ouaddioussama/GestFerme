package com.services.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.implement.FournisseurDao;
import com.dao.interfaces.InterfFournisseurDao;
import com.entities.Fournisseur;

@ManagedBean(name = "FournisseurService")
@ViewScoped
@Service

public class FournisseurService extends ObjectService<Fournisseur> implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@Autowired
	protected Fournisseur objectToInsert;

	@Autowired
	protected InterfFournisseurDao dao;

	public FournisseurService() {

	}

	public Fournisseur getObjectToInsert() {
		return objectToInsert;
	}

	public void setObjectToInsert(Fournisseur objectToInsert) {
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
				listObjects = (List<Fournisseur>) dao.findAll();

			}
			Help.msg = "insere avec Succes";
			reset();

		} else {
			throw new Exception("objectToInsert can not be null");
		}
	}

	public void printAll() {
		System.out.println(dao.findAll().size());

	}

	@PostConstruct
	public void init() {
		System.out.println("kiraaaaa++:" + this.getClass().getSimpleName());

		path = "/views/Fournisseur/index";
		if (objectToInsert == null || listObjects == null || dao == null) {
			reset();
		}
		listObjects = (List<Fournisseur>) dao.findAll();
		System.out.println(listObjects.size());

	}

	public void reset() {
		objectToInsert = new Fournisseur();
		dao =dao==null?new FournisseurDao():dao;
		listObjects = (List<Fournisseur>) dao.findAll();


	}

	public void onRowEdit(RowEditEvent event) throws IOException {

		Fournisseur editedModele = (Fournisseur) event.getObject();

		if (editedModele != null) {
			dao.updateIstance(editedModele);
			Help.msg = "mise e jour faite avec Succes";

		} else {
			System.out.println("objectToInsert is null !");
		}

	}

	public void delete(Fournisseur c) throws Exception {

		if (c != null) {
			dao.deleteInstance(c);
			listObjects.remove(c);
			Help.msg = "supprime avec Succes";

		} else {
			throw new Exception("objectSelected can not be null");
		}
	}

}
