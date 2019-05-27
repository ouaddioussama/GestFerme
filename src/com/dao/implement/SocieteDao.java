package com.dao.implement;

import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfSocieteDao;
import com.entities.Societe;


@Repository
public class SocieteDao extends GenericDao<Societe> implements InterfSocieteDao{
	
	public SocieteDao() 
	{
		super(Societe.class);
	}
	
}

	

