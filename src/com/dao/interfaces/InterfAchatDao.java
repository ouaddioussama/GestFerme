package com.dao.interfaces;

import java.util.List;

import com.Enum.Categorie;
import com.entities.Achat;

public interface InterfAchatDao extends InterfDao<Achat> {

	public List<Achat> getAllByDate(long nbreDay) ;
	   public List<Achat> listAchat(Categorie cat) ;


}