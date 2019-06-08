package com.services.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Enum.Profil;
import com.Enum.Types_Reglement;
import com.dao.implement.EmployeeDao;
import com.entities.Employee;

@ManagedBean(name = "employeeService")
@ViewScoped
@Service

public class EmployeeService extends ObjectService<Employee> implements Serializable   {
	
	
	  /**
	 * 
	 */
	  private static final long serialVersionUID = 1L;
	 @Autowired
	  protected  Employee objectToInsert;
	 
	 @Autowired
	  protected  EmployeeDao  dao;
	 
	  private Set<String> listProfils= new HashSet<String>();

	  
	  public EmployeeService() {
		  
	  }

	  
	  

	  
	
	public Set<String> getListProfils() {
		return listProfils;
	}






	public void setListProfils(Set<String> listProfils) {
		this.listProfils = listProfils;
	}






	public Employee getObjectToInsert() {
		return objectToInsert;
	}





	public void setObjectToInsert(Employee objectToInsert) {
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
				   listObjects = (List<Employee>) dao.findAll();

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

		   path = "/views/employee/index";
		   listObjects = (List<Employee>) dao.findAll();
		   
		   
		   for(Profil s:Profil.values()) {
			   if(s!=Profil.AdminGlobal) {
				    listProfils.add(s.toString());

			   }
			}
		   System.out.println(listObjects.size());
		   
	}
	
	public void reset(){
		objectToInsert = new Employee();
		
	}
	
	public void onRowEdit(RowEditEvent event) throws IOException {
		
		Employee editedModele = (Employee) event.getObject();

		if(editedModele !=null) {
			 dao.updateIstance(editedModele);
			 Help.msg = "mise a jour faite avec Succes";
			
		}else {
			System.out.println("objectToInsert is null !");
		}
	
	} 
	
	
	public void delete(Employee c) throws Exception {
		
		if(c !=null) {
			 dao.deleteInstance(c);
			 listObjects.remove(c);
			 Help.msg = "supprime avec Succes";

			
		}else {
			throw new Exception("objectSelected can not be null");
		}
	}
	

	// Recuperer la liste des employ�es selon type de profil
	public List<Employee> getListResponsable(Profil c) {
		List<Employee> listRespo = null;
		if (listObjects != null) {
			try {
				listRespo = listObjects.stream().filter((p) ->

				p.getProfil() == null ? null : p.getProfil() == c

				).collect(Collectors.toList());

			} catch (Exception e) {

			}

		}

		return listRespo;
	}
	
	public List<Employee> getRespoAchat(){
		return getListResponsable(Profil.Achat);
	}	
	
	public List<Employee> getRespoVente(){
		return getListResponsable(Profil.Vente);
	}
	
	public List<Employee> getRespoCommercial(){
		return getListResponsable(Profil.Commercial);
		
	}	
	
	public List<Employee> getRespoChauffeur(){
		return getListResponsable(Profil.Chauffeur);
		
	}
}
