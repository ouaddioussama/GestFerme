package com.dao.implement;

import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfSecurityDao;
import com.entities.Fich_Remb_Lait;
import com.entities.Security;

@Repository
public class SecurityDao  extends GenericDao<Security>   implements InterfSecurityDao {

	
	public SecurityDao() 
	{
		super(Security.class);
	}
	
	

}
