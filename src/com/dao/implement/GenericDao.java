package com.dao.implement;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.dao.interfaces.InterfDao;

import ma.projet.util.HibernateUtil;

public class GenericDao<T> implements InterfDao<T> {

	protected Class<T> classG;

	// protected T classAttribute;
	String n_package = "com.entities.";

	public GenericDao(Class<T> clazz) {
		this.classG = clazz;
	}

	public List<T> findAll() {
		List<T> ts = null;
		// recuperer le nom de la classe;
		StringBuilder sb = new StringBuilder("from ");
		sb.append(classG.getName());
		//
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		ts = session.createQuery(sb.toString()).list();
		if (ts.size() == 0) {
			ts = Collections.emptyList();
		}

		session.close();
		return ts;
	}

	@Override
	public boolean createInstance(T o) {
		Session session = null;
		try {
			session = openSession();
			session.save(o);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSession(session);
		}

		return true;
	}

	@Override
	public boolean updateIstance(T o) {
		Session session = openSession();
		session.update(o);
		closeSession(session);
		return true;
	}

	@Override
	public boolean deleteInstance(T o) {
		Session session = openSession();
		session.delete(o);
		closeSession(session);
		return true;
	}

	@Override
	public T findById(String id) {
		System.out.println("inside findByid");
		T classinst = null;
		Session session = openSession();
		classinst = (T) session.get(classG.getName(), id);
		closeSession(session);
		return classinst;
	}

	@Override
	public T findById(Integer id) {
		System.out.println("inside findByid");
		T classinst = null;
		Session session = openSession();
		classinst = (T) session.get(classG.getName(), id);
		closeSession(session);
		return classinst;
	}

	public int getCountAll() {
		Session session = openSession();
		int count = ((Long) session.createQuery("select count(*) from " + classG.getName()).uniqueResult()).intValue();
		closeSession(session);
		return count;

	}

	public static Session openSession() {
		// Session session=null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		return session;

	}

	public static void closeSession(Session session) {
		session.getTransaction().commit();

		session.close();

	}

	public List<T> LazyList(int first, int pageSize, String sortField, boolean asc, Map<String, Object> filters) {
		Session session = openSession();

		Criteria cr = session.createCriteria(classG);

		EntityManager em;
		cr.setFirstResult(first);
		cr.setMaxResults(pageSize);
		if (sortField != null) {
			if (asc) {
				cr.addOrder(Order.asc(sortField));
			} else {
				cr.addOrder(Order.desc(sortField));

			}
		}

		return (List<T>) cr.list();

	}

	@Override
	public List<T> LazyList(int first, int pageSize, String sortField, boolean asc) {
		Session session = openSession();

		Criteria cr = session.createCriteria(classG);

		EntityManager em;
		cr.setFirstResult(first);
		cr.setMaxResults(pageSize);
		if (sortField != null) {
			if (asc) {
				cr.addOrder(Order.asc(sortField));
			} else {
				cr.addOrder(Order.desc(sortField));

			}
		}

		return (List<T>) cr.list();

	}



}
