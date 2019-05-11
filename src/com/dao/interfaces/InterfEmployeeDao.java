package com.dao.interfaces;

import com.entities.Employee;

public interface InterfEmployeeDao extends InterfDao<Employee> {
	
	public Employee findByName(String name);
	
}
