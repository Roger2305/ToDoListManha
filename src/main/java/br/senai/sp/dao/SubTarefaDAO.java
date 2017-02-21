package br.senai.sp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.model.SubTarefa;
import br.senai.sp.model.Tarefa;

@Repository
public class SubTarefaDAO {
	@PersistenceContext
	private EntityManager manager;

	@Transactional
	public void criarSubTarefa(Long idTarefa, SubTarefa subtarefa) {
		subtarefa.setTarefa(manager.find(Tarefa.class, idTarefa));
		manager.persist(subtarefa);
	}

	public SubTarefa buscar(Long id) {
		return manager.find(SubTarefa.class, id);
	}
	
	@Transactional
	public void marcarFeito(Long idSubTarefa, boolean valor) {
		SubTarefa subTarefa = buscar(idSubTarefa);
		subTarefa.setFeita(valor);
		manager.merge(subTarefa);
	}
	
	@Transactional
	public void excluir(Long idSubtarefa){
		SubTarefa subtarefa = buscar(idSubtarefa);
		Tarefa tarefa = subtarefa.getTarefa();
		tarefa.getSubtarefas().remove(subtarefa);
		manager.merge(tarefa);
	}
}
