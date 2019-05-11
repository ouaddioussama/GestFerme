package com.services.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.interfaces.InterfFicheSalaireDao;
import com.entities.Fiche_Personnel;

@ManagedBean(name = "ficheSalaireService")
@ViewScoped
@Service

public class FicheSalaireService extends ObjectService<Fiche_Personnel> implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@Autowired
	protected Fiche_Personnel objectToInsert;

	@Autowired
	protected InterfFicheSalaireDao dao;

	public FicheSalaireService() {

	}

	public Fiche_Personnel getObjectToInsert() {
		return objectToInsert;
	}

	public void setObjectToInsert(Fiche_Personnel objectToInsert) {
		this.objectToInsert = objectToInsert;
	}

	public void create() throws Exception {
		System.out.println("inside create");

		if (objectToInsert != null) {
			System.out.println("inside create");
			objectToInsert.setDate_operation(new Date());
			dao.createInstance(objectToInsert);
			try {
				listObjects.add(objectToInsert);

			} catch (Exception e) {
				listObjects = (List<Fiche_Personnel>) dao.findAll();
			}
			Help.msg = "inséré avec Succés";
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

		path = "/views/Fiche_Personnel/index";
		listObjects = (List<Fiche_Personnel>) dao.findAll();
		System.out.println(listObjects.size());

	}

	public void reset() {
		objectToInsert = new Fiche_Personnel();

	}

	public void onRowEdit(RowEditEvent event) throws IOException {

		Fiche_Personnel editedModele = (Fiche_Personnel) event.getObject();

		if (editedModele != null) {
			dao.updateIstance(editedModele);
			Help.msg = "mise à jour faite avec Succès";

		} else {
			System.out.println("objectToInsert is null !");
		}

	}

	public void delete(Fiche_Personnel c) throws Exception {

		if (c != null) {
			dao.deleteInstance(c);
			listObjects.remove(c);
			Help.msg = "supprimé avec Succès";

		} else {
			throw new Exception("objectSelected can not be null");
		}
	}

}
