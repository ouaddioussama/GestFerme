package com.test.dao;

import java.util.List;

import com.dao.implement.ProduitDao;
import com.dao.interfaces.InterfProduitDao;
import com.entities.Produit;

public class ProduitTest {

	public static void main(String[] args) {
		CallMe();
	}
	
	
	public static void tryMe() {
		

		// TODO Auto-generated method stub
		InterfProduitDao dao = new ProduitDao();
		List<Produit> listProd = dao.findAll();
		System.out.println("result:");
		listProd.stream().forEach(a -> {
			Produit p = (Produit) a;
			System.out.println(p.getDesignation_prod());

		}

		);

	
		
	}
	
	public  static void CallMe() {
		ProduitDao p=new ProduitDao();
		System.out.println(p.listAnimaux().size());
	}

}
