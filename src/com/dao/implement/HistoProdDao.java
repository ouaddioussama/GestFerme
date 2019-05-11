package com.dao.implement;

import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfHistoProduitDao;
import com.entities.Historique_Prod;


@Repository
public class HistoProdDao extends GenericDao<Historique_Prod> implements InterfHistoProduitDao{
	
	public HistoProdDao() 
	{
		super(Historique_Prod.class);
	}
	
}

	

