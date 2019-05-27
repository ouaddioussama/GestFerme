package com.services.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.interfaces.InterfSocieteDao;
import com.entities.Societe;

@ManagedBean(name = "societeService")
@ViewScoped
@Service

public class SocieteService extends ObjectService<Societe> implements Serializable   {
	
	
	  /**
	 * 
	 */
	  private static final long serialVersionUID = 1L;
	 @Autowired
	  protected  Societe objectToInsert;
	 
	 
	 @Autowired
	  protected  InterfSocieteDao  dao;
	  
	  public SocieteService() {
		  
	  }

	  

	  
	
	public Societe getObjectToInsert() {
		return objectToInsert;
	}





	public void setObjectToInsert(Societe objectToInsert) {
		this.objectToInsert = objectToInsert;
	}





	public void create() throws Exception{
		System.out.println("inside create");

		if(objectToInsert !=null) {
			
			System.out.println("inside create");
			 dao.createInstance(objectToInsert);
			 try {
				 listObjects.add(objectToInsert);
					
				} catch (Exception e) {
					listObjects=new ArrayList<Societe>();
					 listObjects.add(objectToInsert);
					
				}
			 Help.msg = "insere avec Succes";
			 reset();

		}
		else {
			throw new Exception("objectToInsert can not be null");
		}
	}

 
	public void printAll() {
		System.out.println(dao.findAll().size());
		
	}
	
	@PostConstruct
	public void init() {

		   path = "/views/societe/index";
		   listObjects = (List<Societe>) dao.findAll();
		   System.out.println(listObjects.size());
		   
	}
	
	public void reset(){
		objectToInsert = new Societe();
		
	}
	
	public void onRowEdit(RowEditEvent event) throws IOException {
		
		Societe editedModele = (Societe) event.getObject();

		if(editedModele !=null) {
			 dao.updateIstance(editedModele);
			 Help.msg = "mise e jour faite avec Succes";
			
		}else {
			System.out.println("objectToInsert is null !");
		}
	
	} 
	
	
	public void delete(Societe c) throws Exception {
		
		if(c !=null) {
			 dao.deleteInstance(c);
			 listObjects.remove(c);
			 Help.msg = "supprime avec Succes";

			
		}else {
			throw new Exception("objectSelected can not be null");
		}
	}
	


}
