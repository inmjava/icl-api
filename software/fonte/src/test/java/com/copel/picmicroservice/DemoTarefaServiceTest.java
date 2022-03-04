package com.copel.picmicroservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.copel.picmicroservice.dao.TarefaDAO;
import com.copel.picmicroservice.entidade.Tarefa;
import com.copel.picmicroservice.service.TarefaService;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoTarefaServiceTest {
	
	@Autowired
	private TarefaService tarefaService;

	@MockBean
	private TarefaDAO tarefaDAO;
	
	@BeforeEach
	public void setUp() {
	    Tarefa tarefa = new Tarefa("testar codigo", "fazendo");
	    List<Tarefa> tarefas = List.of(tarefa);
	  	 when(tarefaDAO.findByNomeContainingIgnoreCase(tarefa.getNome()))
	      .thenReturn(tarefas);
	}
	
	
	@Test
	public void quandoBuscaPorNome_retornaTarefa() {
		//given
		String nome = "testar codigo";
		
		//quando
		List<Tarefa> encontradas = tarefaService.findByNome("testar codigo");
		
		//
		assertThat(encontradas).isNotEmpty();
		assertThat(encontradas.get(0).getNome()).isEqualTo(nome);
		
		
		
	}
}
