package com.dao.implement;

import org.springframework.stereotype.Repository;

import com.dao.interfaces.InterfFournisseurDao;
import com.entities.Fournisseur;

@Repository
public class FournisseurDao extends GenericDao<Fournisseur> implements InterfFournisseurDao {

	public FournisseurDao() {
		super(Fournisseur.class);
	}

}
