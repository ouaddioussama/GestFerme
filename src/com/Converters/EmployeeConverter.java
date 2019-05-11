package com.Converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.interfaces.InterfEmployeeDao;
import com.entities.Employee;


@Service
public class EmployeeConverter implements Converter {

	@Autowired
	protected InterfEmployeeDao dao;

	public EmployeeConverter() {

		// System.out.println("inside EmployeeConverter");

	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String beerId) {
		System.out.println("inside getobject");
		System.out.println("value stirng : " + beerId);

		try {

			Employee p = dao.findById(Integer.parseInt(beerId));

			return p;

		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			// return null;
			throw new ConverterException(new FacesMessage(("This is not a valid card id :" + e.getMessage())), e);

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

		// System.out.println("(Employee)object).getLib_Employee()
		// :"+((Employee)object).getLib_Employee());

		return ((Employee) object).getid().toString();

	}

}