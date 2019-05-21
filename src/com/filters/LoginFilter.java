package com.filters;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.GenericFilterBean;

import com.services.bean.LoginService;

@ManagedBean(name = "loginFilter")
@ViewScoped
@Service


public class LoginFilter  extends GenericFilterBean implements Filter {
	
	@Autowired
	protected LoginService loginService;

	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Checks if user is logged in. If not it redirects to the login.xhtml page.
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());

		System.out.println("inside filter ");
	    //do something with your bean
	    boolean propertyValue = loginService.isLogged();
		System.out.println(propertyValue);

		if (loginService == null || !loginService.isLogged() || loginService.getEmployeetoLog()==null) {
			String contextPath = ((HttpServletRequest)request).getContextPath();
			((HttpServletResponse)response).sendRedirect(contextPath + "/login.xhtml");
		}else {
			chain.doFilter(request, response);

		}
		
			
	}

	

}
