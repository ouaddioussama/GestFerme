package com.services.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@RequestScoped
public class FileUploadManagedBean {
	UploadedFile file;

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void fileUploadListener(FileUploadEvent e) {
		// Get uploaded file from the FileUploadEvent
		this.file = e.getFile();
		// Print out the information of the file
		System.out.println(
				"Uploaded File Name Is :: " + file.getFileName() + " :: Uploaded File Size :: " + file.getSize());
		FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String path = servletContext.getRealPath("");
		System.out.println("paaaaaaaaaaaath:" + path);
		String allPath=path+"/resources/images/";
		System.out.println(allPath);
		
		copyFile(allPath);
	}


	
	public void copyFile(String pathDest) {
	       
			   try {
			    
			    InputStream input = file.getInputstream();
			    System.out.println(pathDest);
//			    OutputStream output = new FileOutputStream(new File(pathDest+file.getFileName()));
			    OutputStream output = new FileOutputStream(new File(pathDest+"logo.jpg"));
			    try {
				        IOUtils.copy(input, output);
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			  
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("File Uploaded Successfully"));

			    } catch (IOException ee) {
			        ee.printStackTrace();
			    }
	}

}