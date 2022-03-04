package com.copel.picmicroservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.copel.picmicroservice.dao.TarefaDAO;
import com.copel.picmicroservice.entidade.Tarefa;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DemoTarefaDAOEntityManagerTest {
	
	@Autowired
	TestEntityManager entityManager;

	@Autowired
	TarefaDAO tarefaDAO;
	
	@Test
	public void quandoBuscaPorNome_retornaTarefa() {
		//given
		Tarefa tarefa = new Tarefa("testar codigo", "fazendo");
		entityManager.persist(tarefa);
		entityManager.flush();
		
		//quando
		List<Tarefa> encontradas = tarefaDAO.findByNome("testar codigo");
		
		//
		assertThat(encontradas).isNotEmpty();
		assertThat(encontradas.get(0).getNome()).isEqualTo(tarefa.getNome());
		
		
		
	}
}
