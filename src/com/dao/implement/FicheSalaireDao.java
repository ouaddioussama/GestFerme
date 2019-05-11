package com.dao.implement;

import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfFicheSalaireDao;
import com.entities.Fiche_Personnel;


@Repository
public class FicheSalaireDao extends GenericDao<Fiche_Personnel> implements InterfFicheSalaireDao{
	
	public FicheSalaireDao() 
	{
		super(Fiche_Personnel.class);
	}
	
}

	

