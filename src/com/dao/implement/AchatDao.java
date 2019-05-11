package com.dao.implement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfAchatDao;
import com.entities.Achat;

import ma.projet.util.HibernateUtil;

@Repository
public class AchatDao extends GenericDao<Achat> implements InterfAchatDao {

	public AchatDao() {
		super(Achat.class);
	}

	public List<Achat> getAllByDate(long nbreDay) {
		   System.out.println("debut:"+new Date());

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
		   System.out.println("fin:"+new Date());

		session.close();
		return AchatList;

	}

	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

}
