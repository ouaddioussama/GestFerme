package com.dao.implement;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.Enum.Categorie;
import com.dao.interfaces.InterfProduitDao;
import com.entities.Achat;
import com.entities.Produit;

import ma.projet.util.HibernateUtil;


@Repository
public class ProduitDao extends GenericDao<Produit> implements InterfProduitDao{
	
	public ProduitDao() 
	{
		super(Produit.class);
	}
	
	
	   public List<Produit> listAnimaux( ) {
			Session session = HibernateUtil.getSessionFactory().openSession();
		      Transaction tx = null;
		      List<Produit> employees=null;  
		      try {
		         tx = session.beginTransaction();
		         Criteria cr = session.createCriteria(Produit.class);
		         // Add restriction.
		         cr.add(Restrictions.eq("categorie", Categorie.Animeaux_Achat));
		         employees = cr.list();

		         for (Iterator iterator = employees.iterator(); iterator.hasNext();){
		        	 Produit ach = (Produit) iterator.next(); 
		            System.out.println(" cat: " + ach.getCategorie()); 
		         }
		         tx.commit();
		      } catch (HibernateException e) {
		         if (tx!=null) tx.rollback();
		         e.printStackTrace(); 
		      } finally {
		         session.close(); 
		      }
		   	return employees;

	   }

	
}

	

