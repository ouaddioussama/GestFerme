package com.test.dao;

import java.util.List;

import com.dao.implement.ProduitDao;
import com.dao.interfaces.InterfProduitDao;
import com.entities.Produit;

public class ProduitTest {

	public static void main(String[] args) {
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

}
