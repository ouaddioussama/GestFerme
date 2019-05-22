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

import com.dao.interfaces.InterfRmbLaitDao;
import com.entities.Fich_Remb_Lait;

@ManagedBean(name = "ficheLaitService")
@ViewScoped
@Service

public class FicheLaitService extends ObjectService<Fich_Remb_Lait> implements Serializable   {
	
	
	  /**
	 * 
	 */
	  private static final long serialVersionUID = 1L;
	 @Autowired
	  protected  Fich_Remb_Lait objectToInsert;
	 
	 @Autowired
	  protected  InterfRmbLaitDao  dao;
	  
	  public FicheLaitService() {
		  
	  }

	  

	  
	
	public Fich_Remb_Lait getObjectToInsert() {
		return objectToInsert;
	}





	public void setObjectToInsert(Fich_Remb_Lait objectToInsert) {
		this.objectToInsert = objectToInsert;
	}





	public void create() throws Exception{
		System.out.println("inside create");

		if(objectToInsert !=null) {
			System.out.println("inside create");
			 objectToInsert.setDate_operation(new Date());
			 dao.createInstance(objectToInsert);
			 try {
				 listObjects.add(objectToInsert);

			} catch (Exception e) {
				   listObjects = (List<Fich_Remb_Lait>) dao.findAll();
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

		   listObjects = (List<Fich_Remb_Lait>) dao.findAll();
		   System.out.println(listObjects.size());
		   
	}
	
	public void reset(){
		objectToInsert = new Fich_Remb_Lait();
		
	}
	
	public void onRowEdit(RowEditEvent event) throws IOException {
		
		Fich_Remb_Lait editedModele = (Fich_Remb_Lait) event.getObject();

		if(editedModele !=null) {
			 dao.updateIstance(editedModele);
			 Help.msg = "mise e jour faite avec Succes";
			
		}else {
			System.out.println("objectToInsert is null !");
		}
	
	} 
	
	
	public void delete(Fich_Remb_Lait c) throws Exception {
		
		if(c !=null) {
			 dao.deleteInstance(c);
			 listObjects.remove(c);
			 Help.msg = "supprime avec Succes";

			
		}else {
			throw new Exception("objectSelected can not be null");
		}
	}
	


}
