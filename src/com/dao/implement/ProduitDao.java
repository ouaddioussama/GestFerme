package com.dao.implement;

import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfProduitDao;
import com.entities.Produit;


@Repository
public class ProduitDao extends GenericDao<Produit> implements InterfProduitDao{
	
	public ProduitDao() 
	{
		super(Produit.class);
	}
	
}

	

