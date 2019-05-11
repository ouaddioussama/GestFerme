package com.dao.implement;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfClientDao;
import com.entities.Client;


@Repository
public class ClientDao extends GenericDao<Client> implements InterfClientDao{
	
	public ClientDao() 
	{
		super(Client.class);
	}
	
}

	

