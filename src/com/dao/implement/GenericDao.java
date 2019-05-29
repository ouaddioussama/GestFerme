package com.dao.implement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.LazyDataModel;

import com.Enum.Categorie;
import com.dao.interfaces.InterfDao;

import ma.projet.util.HibernateUtil;

public class GenericDao<T> extends LazyDataModel<T> implements InterfDao<T> {

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
	/*
	 * public List<T> LazyList(int first, int pageSize, String sortField, boolean
	 * asc, Map<String, Object> filters) { Session session = openSession();
	 * 
	 * Criteria cr = session.createCriteria(classG);
	 * 
	 * EntityManager em; cr.setFirstResult(first); cr.setMaxResults(pageSize); if
	 * (sortField != null) { if (asc) { cr.addOrder(Order.asc(sortField)); } else {
	 * cr.addOrder(Order.desc(sortField));
	 * 
	 * } }
	 * 
	 * return (List<T>) cr.list();
	 * 
	 * }
	 */

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

	@Override
	public List<T> LazyList(int first, int pageSize, String sortField, boolean asc, Categorie c) {
		Session session = openSession();

		Criteria cr = session.createCriteria(classG);
		cr.createAlias("produit", "p");
		// Add restriction.
		cr.add(Restrictions.eq("p.categorie", c));

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
	public List<T> LazyList(int first, int pageSize, String sortField, boolean asc, Map<String, Object> filters,
			Categorie c) {
		Session session = openSession();

		Criteria cr = session.createCriteria(classG);
		cr.createAlias("produit", "p");
		// Add restriction.
		cr.add(Restrictions.eq("p.categorie", c));

		EntityManager em;
		cr.setFirstResult(first);
		cr.setMaxResults(pageSize);

		/** Traitement des filtres **/
		// List<Criterion> listRestrictions=new ArrayList<Criterion>();
		Conjunction conjunction = Restrictions.conjunction();

		if (filters != null && filters.size() > 0) {
			for (Map.Entry<String, Object> entry : filters.entrySet()) {
				String field = entry.getKey();
				String value = "";
				int intVal = 0;
				System.out.println(entry.getKey().getClass());

				try {

					 value = entry.getValue().toString();
					 /*
					if (entry.getValue() instanceof Integer) {
						System.out.println("data is integer , value is :" + entry.getValue());
						 value = entry.getValue().toString();
					} else if (entry.getValue() instanceof String) {
						System.out.println("data is String , value is :" + entry.getValue());
						value =  entry.getValue().toString().trim();
					} else if (entry.getValue() instanceof Float) {
						System.out.println("data is Float , value is :" + entry.getValue());
						value = String.valueOf(entry.getValue());
					} else if (entry.getValue() instanceof Date) {
						System.out.println("data is Date , value is :" + entry.getValue());
						DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
						value = dateFormat.format(entry.getValue());

					} else {
						System.out.println("data is else , value is :" + entry.getValue());

					} */

					System.out.println("finished try , value debut :" + value + "value fin");
					System.out.println("finished try , value class :" + value.getClass());

				
               /*
				if (value == null) {
					continue;
				}
				*/
				
				System.out.println("fileds "+field);

//				conjunction.add(Restrictions.eqProperty("p."+field, value));
//				conjunction.add(Restrictions.like("p.id", value, MatchMode.ANYWHERE));
				conjunction.add(Restrictions.sqlRestriction(field+" LIKE '%"+value+"%' "));
				/*
				 if(isNumeric(value)) {
						
						conjunction.add(Restrictions.like(field, String.valueOf(Integer.valueOf(value)),MatchMode.ANYWHERE));

				 } else {
						conjunction.add(Restrictions.eq(field, value));
					 
				 } */

				
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

		cr.add(conjunction);

		/** Traitement des sorts **/

		if (sortField != null) {
			if (asc) {
				cr.addOrder(Order.asc(sortField));
			} else {
				cr.addOrder(Order.desc(sortField));

			}
		}

		return (List<T>) cr.list();

	}
	
	public  boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}

}
