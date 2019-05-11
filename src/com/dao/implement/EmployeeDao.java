package com.dao.implement;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfEmployeeDao;
import com.entities.Employee;

import ma.projet.util.HibernateUtil;


@Repository
public class EmployeeDao extends GenericDao<Employee> implements InterfEmployeeDao{
	
	public EmployeeDao() 
	{
		super(Employee.class);
	}

	@Override
	public Employee findByName(String name) {
		List<Employee> ts = null;
		Employee emp=null;
		
		// recuperer le nom de la classe;
		StringBuilder sb = new StringBuilder("from Employee");
		//
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria cr = session.createCriteria(classG);
		cr.add(Restrictions.eq("nom", name));
		ts = cr.list();
		
		//ts = session.createQuery(sb.toString()).list();
		if (ts.size() == 0) {
			ts = Collections.emptyList();
		} else {
			emp=ts.get(0);
		}

		session.close();
		return emp;
	}
	
}

	

