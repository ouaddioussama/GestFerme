package com.services.bean;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.interfaces.InterfEmployeeDao;
import com.entities.Employee;

@ManagedBean(name = "loginService")
@SessionScoped
@Service

public class LoginService {

	@Autowired
	private Employee EmployeetoLog;
	private String log_out_path = "/login";

	@Autowired
	protected InterfEmployeeDao dao;

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

	public void login() throws IOException {
		System.out.println("inside login");

		if (EmployeetoLog != null) {
			Employee emp = dao.findByName(EmployeetoLog.getNom());

//			System.out.println("nom:" + EmployeetoLog.getNom());
//			System.out.println("pass:" + EmployeetoLog.getPass_word());
//
//			System.out.println("nom:" + emp.getNom());
//			System.out.println("pass:" + emp.getPass_word());

			if (emp != null && (EmployeetoLog.getNom().equals(emp.getNom()))
					&& (EmployeetoLog.getPass_word().equals(emp.getPass_word()))) {

				EmployeetoLog=emp;

				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedEmployee",
						EmployeetoLog);
				

				Help.msg = "Bienvenue " + emp.getNom();
				help.goRefresh("/views/acceuil");

			} else if (EmployeetoLog != null && (EmployeetoLog.getNom().equals("admin"))
					&& (EmployeetoLog.getPass_word().equals("admin"))) {

				EmployeetoLog=emp;

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

	public String logOut() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return log_out_path;
	}

}
