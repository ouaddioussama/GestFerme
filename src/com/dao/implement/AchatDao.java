package com.dao.implement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.Enum.Categorie;
import com.dao.interfaces.InterfAchatDao;
import com.entities.Achat;

import ma.projet.util.HibernateUtil;

@Repository
public class AchatDao extends GenericDao<Achat> implements InterfAchatDao {

	public AchatDao() {
		super(Achat.class);
	}

	public List<Achat> getAllByDate(long nbreDay) {
		System.out.println("debut:" + new Date());

		DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;

		String formattedDate = formatter.format(LocalDate.now());
		String formattedDate1 = "";
		if (nbreDay >= 0) {
			formattedDate1 = formatter.format(LocalDate.now().plusDays(nbreDay));

		} else {
			formattedDate1 = formatter.format(LocalDate.now().minusDays(Math.abs(nbreDay)));

		}
		System.out.println(formattedDate);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date minDate = null;
		Date maxDate = null;

		if (nbreDay >= 0) {
			try {
				minDate = format.parse(formattedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				maxDate = format.parse(formattedDate1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				minDate = format.parse(formattedDate1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				maxDate = format.parse(formattedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Achat> AchatList = null;

		// recuperer le nom de la classe;
		StringBuilder sb = new StringBuilder("from Achat");
		//
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Criteria cr = session.createCriteria(classG);
		// cr.add(Restrictions.gt("dateOperation", minDate));
		// cr.add(Restrictions.gt("dateOperation", minDate));

		Conjunction and = Restrictions.conjunction();
		and.add(Restrictions.ge("dateDePaiement", minDate));
		and.add(Restrictions.lt("dateDePaiement", maxDate));

		cr.add(and);

		AchatList = cr.list();

		// ts = session.createQuery(sb.toString()).list();
		if (AchatList.size() == 0) {
			AchatList = Collections.emptyList();
		}
		System.out.println("fin:" + new Date());

		session.close();
		return AchatList;

	}

	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

	public List<Achat> listAchat(Categorie cat) {
		List<Achat> AchatList = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Criteria cr = session.createCriteria(Achat.class);
		cr.createAlias("produit", "p");
		cr.add(Restrictions.eq("p.categorie", cat));

		AchatList = cr.list();

		// ts = session.createQuery(sb.toString()).list();
		if (AchatList.size() == 0) {
			AchatList = Collections.emptyList();
		}

		session.close();
		return AchatList;

	}

	public List<Achat> listAnimaux2() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<Achat> employees = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Achat.class);
			cr.createAlias("produit", "p");
			// Add restriction.
			cr.add(Restrictions.eq("p.categorie", Categorie.Animeaux_Achat));
			employees = cr.list();

			for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
				Achat ach = (Achat) iterator.next();
				// System.out.println(" cat: " + ach.getProduit().getCategorie());
				System.out.println(" code: " + ach.getId());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employees;

	}

	public List<Achat> getListAchat() {
		// Session session = HibernateUtil.getSessionFactory().openSession();

		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Achat> criteriaQuery = cb.createQuery(Achat.class);
		Root<Achat> root = criteriaQuery.from(Achat.class);
		CriteriaQuery<Achat> cr = criteriaQuery.select(root);
		TypedQuery<Achat> query = em.createQuery(cr);

		List<Achat> list = query.getResultList();
		return list;
	}

	public List<Object[]>  getProjectionsData() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Criteria crit = session.createCriteria(Achat.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.max("montant_global"));
		projList.add(Projections.min("montant_global"));
		projList.add(Projections.avg("montant_global"));
		projList.add(Projections.countDistinct("montant_global"));
		crit.setProjection(projList);
		List<Object[]> results = crit.list();
		return results;
	}
}
