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

import com.dao.implement.ClientDao;
import com.entities.Client;

@ManagedBean(name = "clientService")
@ViewScoped
@Service

public class ClientService extends ObjectService<Client> implements Serializable   {
	
	
	  /**
	 * 
	 */
	  private static final long serialVersionUID = 1L;
	 @Autowired
	  protected  Client objectToInsert;
	 
	 @Autowired
	  protected  ClientDao  dao;
	  
	  public ClientService() {
		  
	  }

	  

	  
	
	public Client getObjectToInsert() {
		return objectToInsert;
	}





	public void setObjectToInsert(Client objectToInsert) {
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
				   listObjects = (List<Client>) dao.findAll();
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

		   path = "/views/Client/index";
		   listObjects = (List<Client>) dao.findAll();
		   System.out.println(listObjects.size());
		   
	}
	
	public void reset(){
		objectToInsert = new Client();
		
	}
	
	public void onRowEdit(RowEditEvent event) throws IOException {
		
		Client editedModele = (Client) event.getObject();

		if(editedModele !=null) {
			 dao.updateIstance(editedModele);
			 Help.msg = "mise a jour faite avec Succes";
			
		}else {
			System.out.println("objectToInsert is null !");
		}
	
	} 
	
	
	public void delete(Client c) throws Exception {
		
		if(c !=null) {
			 dao.deleteInstance(c);
			 listObjects.remove(c);
			 Help.msg = "supprime avec Succes";

			
		}else {
			throw new Exception("objectSelected can not be null");
		}
	}
	


}
