package com.test.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.dao.implement.AchatDao;
import com.entities.Achat;
import com.services.bean.AchatService;

public class AchatServiceTest {

	public static void main(String[] args) throws ParseException {
		
		AchatService aserv=new AchatService();
		List a =new AchatDao().findAll();
		aserv.setListObjects(a);
		System.out.println(aserv.getListAchatAgricoleD(3).size());


	}

}
