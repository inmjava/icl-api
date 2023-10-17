package com.copel.icl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.copel.icl.dao.TarefaDAO;
import com.copel.icl.entidade.Tarefa;
import com.copel.icl.excecao.NotFoundException;

@Service
public class TarefaService {
	
	private static final Logger logger = LoggerFactory.getLogger(TarefaService.class);

	@Autowired
	private TarefaDAO tarefaDAO;


	public Page<Tarefa> allPaginada (Pageable paginacao) {
		Page<Tarefa> all = tarefaDAO.findAll(paginacao);
		return all;
	}
	
	public List<Tarefa> all () {
		List<Tarefa> all = tarefaDAO.findAll();
		return all;
	}

	
	public Tarefa novaTarefa(Tarefa novaTarefa) {
		Tarefa tarefaSalva = tarefaDAO.save(new Tarefa(novaTarefa.getNome(), novaTarefa.getStatus()));
		return tarefaSalva;
	}

	public Optional<Tarefa> findById(Long id) {
		Optional<Tarefa> tarefa = tarefaDAO.findById(id);
		return tarefa;
	}

	
	public Tarefa alterar(Tarefa tarefa, Long id) {
		Optional<Tarefa> tarefaOpt = tarefaDAO.findById(id);
		if (tarefaOpt.isPresent()) {
			Tarefa tarefaExistente = tarefaOpt.get();
			tarefaExistente.setNome(tarefa.getNome());
			tarefaExistente.setStatus(tarefa.getStatus());
			Tarefa tarefaAlterada = tarefaDAO.save(tarefaExistente);
			return tarefaAlterada;
		} else {
			throw new NotFoundException("Tarefa não localizada com id=" + id);
		}
	}
	
	public void excluir(Long id) {
		tarefaDAO.deleteById(id);
	}

	
	public List<Tarefa> findByNome (String nome) {
		List<Tarefa> tarefas = new ArrayList<>();
		tarefaDAO.findByNomeContainingIgnoreCase(nome).forEach(tarefas::add);
		return tarefas;	
	}

	public List<Tarefa> findByNomeEStatus(String nome, String status) {
		List<Tarefa> tarefas = new ArrayList<>();
		tarefaDAO.findByNomeContainingIgnoreCaseAndStatus(nome, status).forEach(tarefas::add);
		return tarefas;

	}
}