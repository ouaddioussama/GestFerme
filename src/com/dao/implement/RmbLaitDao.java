package com.dao.implement;

import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfRmbLaitDao;
import com.entities.Fich_Remb_Lait;


@Repository
public class RmbLaitDao extends GenericDao<Fich_Remb_Lait> implements InterfRmbLaitDao{
	
	public RmbLaitDao() 
	{
		super(Fich_Remb_Lait.class);
	}
	
}

	

