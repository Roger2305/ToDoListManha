package br.senai.sp.dao;

import java.util.List;

public interface interfaceDAO<T> {
	public void inserir(T objeto);

	public T buscar(Long id);

	public List<T> listar();

	public void excluir(Long id);
}
