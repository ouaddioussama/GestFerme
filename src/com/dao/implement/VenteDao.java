package com.dao.implement;

import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfVenteDao;
import com.entities.Vente;


@Repository
public class VenteDao extends GenericDao<Vente> implements InterfVenteDao{
	
	public VenteDao() 
	{
		super(Vente.class);
	}
	
	
	
}

	

