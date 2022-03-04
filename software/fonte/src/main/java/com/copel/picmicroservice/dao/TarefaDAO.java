package com.copel.picmicroservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.copel.picmicroservice.entidade.Tarefa;

public interface TarefaDAO extends JpaRepository<Tarefa, Long> {
	  public List<Tarefa> findByNome(String nome);
	  public List<Tarefa> findByStatus(String status);
	  
	  public List<Tarefa> findByNomeContainingIgnoreCase(String nome); 
	  public List<Tarefa> findByNomeContainingIgnoreCaseAndStatus(String nome, String status);
	
}
