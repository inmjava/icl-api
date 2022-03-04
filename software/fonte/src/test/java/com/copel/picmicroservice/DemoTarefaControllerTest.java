package com.copel.picmicroservice;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.copel.picmicroservice.controller.TarefaController;
import com.copel.picmicroservice.entidade.Tarefa;
import com.copel.picmicroservice.service.TarefaService;


@WebMvcTest(TarefaController.class)
public class DemoTarefaControllerTest {
	
	private static Logger logger = Logger.getLogger(DemoTarefaControllerTest.class);
	
	@Autowired
    private MockMvc mvc;

	@MockBean
	private TarefaService tarefaService;
	
	@Test
	public void quandoBuscaTodos_retornaLista() throws Exception {
		//mock da dependencia interna do controller (servico)
		Tarefa tarefa = new Tarefa("testar codigo", "fazendo");
	    List<Tarefa> tarefas = Arrays.asList(tarefa);
	    when(tarefaService.all()).thenReturn(tarefas);
		
		//invoca o controller instanciado no contexto da aplicaco, porem ser um http server por baixo
	    MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/tarefas")
	    	      .contentType(MediaType.APPLICATION_JSON))
	    		
	    	      .andExpect(MockMvcResultMatchers.status().isOk())
	    	      .andDo( MockMvcResultHandlers.print() ).andReturn();
	    
		String body = result.getResponse().getContentAsString();
		
		logger.info(body);

		
	}
}
