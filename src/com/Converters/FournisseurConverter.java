package com.Converters;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.implement.FournisseurDao;
import com.dao.interfaces.InterfFournisseurDao;
import com.entities.Fournisseur;



 
@Service

public class FournisseurConverter implements Converter {
	
	@Autowired
	private InterfFournisseurDao daofou;

	public  FournisseurConverter(){
		
		//System.out.println("inside FournisseurConverter");
		
	}
	
	
	
	 @Override
	    public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String beerId) {
		 System.out.println("inside getobject");
		System.out.println("value stirng : "+beerId);
		 
		
		 try {   

			 Fournisseur p =daofou.findById(Integer.parseInt(beerId));

		
			 return p;
		     
		    	 
		    }
		         catch (NumberFormatException e) {
		        	 System.out.println(e.getMessage());
		        	 //return null;
		             throw new ConverterException(new FacesMessage(("This is not a valid card id :"+e.getMessage())), e);

		         }   
	
	    }

	
	 
	    @Override
	    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
	    	System.out.println("inside getString in converters!");
	    	
	    	if (object == null || object.equals("")) {
	            return "";
	        }
	        if (facesContext == null) {
	            throw new NullPointerException("context");
	        }
	        if (uiComponent == null) {
	            throw new NullPointerException("component");
	        }
	        
	         // System.out.println("(Fournisseur)object).getLib_Fournisseur() :"+((Fournisseur)object).getLib_Fournisseur());
	          
	        return ((Fournisseur)object).getid().toString();
	    

	}  
	    
	   
	    
}