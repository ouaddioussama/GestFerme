package com.dao.interfaces;
import java.util.List;


public interface InterfDao<T> {
	public boolean createInstance(T o);
	public boolean updateIstance(T o);
	public boolean deleteInstance(T o) ;
	public List<T> findAll();
	public T findById(Integer id);
	public T findById(String id);
	public int getCountAll() ;
	public List<T> LazyList(int first, int pageSize, String sortField, boolean asc);




}
