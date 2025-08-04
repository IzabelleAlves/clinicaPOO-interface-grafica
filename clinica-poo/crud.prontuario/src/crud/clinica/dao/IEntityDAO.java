package crud.clinica.dao;

import java.sql.SQLException;
import java.util.List;

import crud.clinica.exception.CPFJaExisteException;

public interface IEntityDAO <T> {

	public void create(T t) throws SQLException, CPFJaExisteException;
	
	public T findById(Long id);
	
	public void delete(T t);
	
	public List<T> findAll();
	
	public void update(T t) throws SQLException, CPFJaExisteException;
	
//	public T findByCPF(String s);
}
