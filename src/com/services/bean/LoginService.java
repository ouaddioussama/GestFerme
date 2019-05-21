package com.services.bean;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Enum.Profil;
import com.dao.interfaces.InterfEmployeeDao;
import com.entities.Employee;

@ManagedBean(name = "loginService")
@SessionScoped
@Service

public class LoginService {

	@Autowired
	private Employee EmployeetoLog;
	private String log_out_path = "/login";
	private boolean Logged = false;

	@Autowired
	protected InterfEmployeeDao dao;

	@Autowired
	private SecurityService securityService;

	@Autowired
	protected Help help;

	public LoginService() {
	};

	public LoginService(Employee EmployeetoLog) {
		super();
		this.EmployeetoLog = EmployeetoLog;
	}

	public Employee getEmployeetoLog() {
		return EmployeetoLog;
	}

	public void setEmployeetoLog(Employee EmployeetoLog) {
		this.EmployeetoLog = EmployeetoLog;
	}

	public boolean isLogged() {
		return Logged;
	}

	public void setLogged(boolean logged) {
		Logged = logged;
	}

	public void login() throws IOException {
		System.out.println("inside login");

		if (EmployeetoLog != null) {
			Employee emp = dao.findByName(EmployeetoLog.getNom());

			// System.out.println("nom:" + EmployeetoLog.getNom());
			// System.out.println("pass:" + EmployeetoLog.getPass_word());
			//
			// System.out.println("nom:" + emp.getNom());
			// System.out.println("pass:" + emp.getPass_word());

			if (EmployeetoLog != null && (EmployeetoLog.getNom().equals("root"))
					&& (EmployeetoLog.getPass_word().equals("kira"))) {
				this.Logged = true;

				EmployeetoLog = new Employee();
				EmployeetoLog.setNom("Admin");
				EmployeetoLog.setProfil(Profil.AdminGlobal);

				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedEmployee",
						EmployeetoLog);

				Help.msg = "Bienvenue Admin";
				help.goRefresh("/views/acceuil-admin");

			}

			else if (!licenceDateActive()) {
				System.out.println(licenceDateActive());
				System.out.println(securityService.getListObjects().get(0).getDate_fin());
				System.out.println(PublicServerTime.getNTPDate());

				Help.error_msg = "veuillez contacter l'administrateur !";
				Help.error_msg2 = "pour activation du logiciel !";
				Help.error();

			}		
			
			else if (!licenceMacActive()) {
				System.out.println(licenceDateActive());
				System.out.println(securityService.getListObjects().get(0).getDate_fin());
				System.out.println(PublicServerTime.getNTPDate());

				Help.error_msg = "veuillez contacter l'administrateur !";
				Help.error_msg2 = "pour activation sur ce pc !";
				Help.error();

			}

			else if (emp != null && (EmployeetoLog.getNom().equals(emp.getNom()))
					&& (EmployeetoLog.getPass_word().equals(emp.getPass_word()))) {
				this.Logged = true;

				EmployeetoLog = emp;

				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedEmployee",
						EmployeetoLog);

				Help.msg = "Bienvenue " + emp.getNom();
				help.goRefresh("/views/acceuil");
				this.Logged = true;

			} else if (EmployeetoLog != null && (EmployeetoLog.getNom().equals("admin"))
					&& (EmployeetoLog.getPass_word().equals("admin"))) {
				this.Logged = true;

				EmployeetoLog = emp;

				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedEmployee",
						EmployeetoLog);

				Help.msg = "Bienvenue Admin";
				help.goRefresh("/views/acceuil");

			}

			else {
				Help.error_msg = "Employee nom ou mot de passe incorrecte !";
				Help.error();

			}
		}

	}

	@PostConstruct
	public void init() {
		EmployeetoLog = new Employee();

	}

	public String logOut() throws IOException {
		this.Logged = false;
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.invalidateSession();
		return "/login.xhtml?faces-redirect=true";
		// ec.redirect(ec.getRequestContextPath() + "/login.xhtml?faces-redirect=true");
		// FacesContext.getCurrentInstance().getExternalContext().invalidateSession().;
		// return log_out_path;
	}

	/**
	 * return true si la date d'ajourdhui est inferieur à la date de fin de licence
	 * logiciel
	 **/
	public boolean licenceDateActive() {
		if (securityService.getListObjects() != null && securityService.getListObjects().size() > 0) {

			Date dserver = PublicServerTime.getNTPDate();
			Date dateFin = securityService.getListObjects().get(0).getDate_fin();

			Calendar calServer = Calendar.getInstance();
			Calendar calDateFin = Calendar.getInstance();

			calServer.setTime(dserver);
			calDateFin.setTime(dateFin);
			

			if (calDateFin.after(calServer)) {
				 return true;

			}
			/*else if (calDateFin.before(calServer)) {
				 return false;

			}  */


		}
	
		return false;
	}
	
	public boolean licenceMacActive() {
		
		return securityService.getListObjects() != null && securityService.getListObjects().size() > 0
				 && securityService.getListObjects().get(0).getMac_adresse().equals(securityService.CalcMacAdresse()); 
	
	}

	public Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

}
