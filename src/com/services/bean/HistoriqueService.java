package com.services.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.implement.HistoProdDao;
import com.dao.interfaces.InterfHistoProduitDao;
import com.entities.Historique_Prod;


@ManagedBean(name = "historiqueService")
@ViewScoped
@Service
public class HistoriqueService extends ObjectService<Historique_Prod> implements Serializable  {

	  private static final long serialVersionUID = 1L;
		 @Autowired
		 protected  Historique_Prod objectToInsert;
		 
		 
		 @Autowired
		 protected  InterfHistoProduitDao  histdao;
		  
		  public HistoriqueService() {
			  
		  }

		public Historique_Prod getObjectToInsert() {
			return objectToInsert;
		}

		public void setObjectToInsert(Historique_Prod objectToInsert) {
			this.objectToInsert = objectToInsert;
		}
		  
		public void create() throws Exception{
			System.out.println("inside create");

			if(objectToInsert !=null) {
				
				System.out.println("inside create");
				if(histdao==null) {
					histdao=new HistoProdDao();
				}
				histdao.createInstance(objectToInsert);
				 try {
					 listObjects.add(objectToInsert);
						
					} catch (Exception e) {
						 listObjects = (List<Historique_Prod>) histdao.findAll();
						 listObjects.add(objectToInsert);
						
					}
 
				 //Help.msg = "inséré avec Succés";
				 reset();

			}
			else {
				throw new Exception("objectToInsert can not be null");
			}
		}

	 
		public void printAll() {
			System.out.println(histdao.findAll().size());
			
		}
		
		@PostConstruct
		public void init() {

			   path = "/views/Historique_Prod/index";
			   listObjects = (List<Historique_Prod>) histdao.findAll();
			   System.out.println(listObjects.size());
			   
		} 
		
		public void reset(){
			objectToInsert = new Historique_Prod();
			
		}
		
		public List<Historique_Prod> getListObjects() {
			if(histdao !=null) {
			   listObjects=(List<Historique_Prod>)histdao.findAll();

			}
			return listObjects;
		}

}
