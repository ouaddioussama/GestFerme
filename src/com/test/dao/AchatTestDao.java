package com.test.dao;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.dao.implement.AchatDao;

public class AchatTestDao {

	public static void main(String[] args) throws ParseException {

	
		AchatDao dao = new AchatDao();

		//System.out.println(dao.getAllByDate(-3).size());
		LocalDate d1=LocalDate.now();
		LocalDate d2=LocalDate.now().plusDays(3);
		
		//long daysBetween = Days.between(d1, d2);
		System.out.println("Until christmas: " + d1.until(d2));
		System.out.println("Until christmas (with crono): " + d1.until(d2, ChronoUnit.DAYS));

	}

}
