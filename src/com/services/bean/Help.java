
package com.services.bean;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

@ManagedBean(name = "HelpBean")
@SessionScoped
@Service

public class Help {

	static String msg;
	static String error_msg;
	static String error_msg2;
	public static String path = "/views/acceuil.xhtml";
	public static String contextPath = "";

	public static void anotherDisplay() {
		if (msg != null && msg.length() > 0) {
			displayMsg(msg);

		} else if (error_msg != null && error_msg.length() > 0) {
			error();

		}
		Help.msg = "";
		error_msg = "";

	}

	public static void displayMsg(String msg) {

		if (msg != null && !msg.isEmpty()) {
			System.out.println("kira:" + msg);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}
	/*
	 * public static void goTo(String path) throws IOException{ String patth=
	 * FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath(
	 * ); System.out.println(patth);
	 * FacesContext.getCurrentInstance().getExternalContext().redirect(patth+path+
	 * ".xhtml");
	 * 
	 * 
	 * }
	 */

	public static String goTo(String path) throws IOException {
		String patth = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		System.out.println(patth);
		FacesContext.getCurrentInstance().getExternalContext().redirect(patth + path + ".xhtml?faces-redirect=true");
		return "";

	}

	public String goHome() throws IOException {
		go("/views/acceuil.xhtml");
		goTo("/views/index");
		return "";
	}

	public String goRefresh(String p) throws IOException {

		go(p+".xhtml");
		goTo("/views/index");
		return "";
	}

	public static String goToServlet(String path) throws IOException {
		String patth = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		System.out.println(patth);
		FacesContext.getCurrentInstance().getExternalContext().redirect(patth + path + "?faces-redirect=true");
		return "";

	}

	public void info() {
		System.out.println("info");
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "PrimeFaces Rocks."));
	}

	public void warn() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, error_msg, "Watch out for PrimeFaces."));
		error_msg = "";
	}

	public static void error() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, error_msg, error_msg2));
		error_msg = "";
		error_msg2 = "";
	}

	public void fatal() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, error_msg, "System Error"));
		error_msg = "";
	}

	public String getKira() {
		System.out.println(" yo kira !");
		return "";
	}

	public String doSomeAction() {
		System.out.println("CommandLink is used");
		return "";
	}

	public void forward(String uri) throws IOException {
		// String uri = "destination.xhtml";
		FacesContext.getCurrentInstance().getExternalContext().dispatch(uri);
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public void go(String p) {
		this.path = p;
	}

	public void createPDF() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpSession session = (HttpSession) externalContext.getSession(true);
		String url = "http://localhost:8087/_Gest_Commerc_Oussama/test.xhtml;JSESSIONID=" + session.getId();
		try {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(url);
			renderer.layout();
			HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
			response.reset();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "C://user//first.pdf");
			OutputStream browserStream = response.getOutputStream();
			renderer.createPDF(browserStream);
			browserStream.close();
			session.invalidate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		facesContext.responseComplete();
	}

	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		contextPath = servletContext.getRealPath("");

	}
	
	 public String logout() {
	        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	        return "/login.xhtml?faces-redirect=true";
	    }
}
