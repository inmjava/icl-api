package com.copel.picmicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.copel.icl.entidade.Tarefa;

@SpringBootTest (webEnvironment = WebEnvironment.RANDOM_PORT)
class TarefaTest {
	private static final Logger logger = LoggerFactory.getLogger(TarefaTest.class);
	

	@LocalServerPort
	private int port;

	public String getUrlBase() {
		String urlBase =  "http://localhost:"+ port +"/" + ConstantesTest.CONTEXT +  "/tarefas";
		return urlBase;
	}
	
	@Test
	public void testPost() throws Exception {

		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate template = builder.build();

		HttpHeaders headers = new HttpHeaders();

		Tarefa tarefa = new Tarefa("sprint-1", "andamento");
		HttpEntity<Tarefa> request = new HttpEntity<>(tarefa, headers);

		ResponseEntity<Tarefa> response = template.postForEntity(getUrlBase(), request, Tarefa.class);

		assertEquals(HttpStatus.SC_CREATED, response.getStatusCodeValue());
		logger.info(response.getBody().toString());
	}

	@Test
	public void testList() throws Exception {

		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate template = builder.build();

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Tarefa> request = new HttpEntity<>(null, headers);

		
		ResponseEntity<List<Tarefa>> response = template.exchange(getUrlBase(), HttpMethod.GET, request,
				new ParameterizedTypeReference<List<Tarefa>>() {
				});
		assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());

		logger.info(response.getBody().toString());
	}

	
	@Test
	public void testGet() throws Exception {

		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate template = builder.build();

		HttpHeaders headers = new HttpHeaders();	
		Tarefa tarefa = new Tarefa("sprint-1", "andamento");
		HttpEntity<Tarefa> request = new HttpEntity<>(tarefa, headers);
		ResponseEntity<Tarefa> responsePost = template.postForEntity(getUrlBase(), request, Tarefa.class);
		
		
		ResponseEntity<Tarefa> response = template.getForEntity(getUrlBase() + "/" + responsePost.getBody().getId(), Tarefa.class);
		assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());

		logger.info(response.getBody().toString());
	}
	
	
	@Test
	public void testDelete() throws Exception {

		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate template = builder.build();

		//insere 
		HttpHeaders headers = new HttpHeaders();
		Tarefa tarefa = new Tarefa("sprint1", "andamento");
		HttpEntity<Tarefa> request = new HttpEntity<>(tarefa, headers);
		ResponseEntity<Tarefa> response = template.postForEntity(getUrlBase(), request, Tarefa.class);
		Long id = response.getBody().getId();
		
		//deleta
		headers = new HttpHeaders();
		request = new HttpEntity<>(null, headers);
		template.delete(getUrlBase() + "/" + id);
		
		
	
	}
	
	
	@Test
	public void testFindByNome() throws Exception {

		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate template = builder.build();

		//insere 
		HttpHeaders headers = new HttpHeaders();
		Tarefa tarefa = new Tarefa("sprint1", "andamento");
		HttpEntity<Tarefa> request = new HttpEntity<>(tarefa, headers);
		ResponseEntity<Tarefa> response = template.postForEntity(getUrlBase(), request, Tarefa.class);
		Long id = response.getBody().getId();
		
		//pesquisa
		headers = new HttpHeaders();
		request = new HttpEntity<>(null, headers);
		ResponseEntity<List<Tarefa>> responsePesquisa = template.exchange(getUrlBase()  + "?nome=" + response.getBody().getNome(), HttpMethod.GET, request,
				new ParameterizedTypeReference<List<Tarefa>>() {
				});
		
		assertEquals(HttpStatus.SC_OK, responsePesquisa.getStatusCodeValue());
		logger.info(response.getBody().toString());
		
		
	
	}
	
	
	

	@Test
	public void testFindByNomeEStatus() throws Exception {

		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate template = builder.build();

		//insere 
		HttpHeaders headers = new HttpHeaders();
		Tarefa tarefa = new Tarefa("sprint1", "andamento");
		HttpEntity<Tarefa> request = new HttpEntity<>(tarefa, headers);
		ResponseEntity<Tarefa> response = template.postForEntity(getUrlBase(), request, Tarefa.class);
		Long id = response.getBody().getId();
		
		//pesquisa
		headers = new HttpHeaders();
		request = new HttpEntity<>(null, headers);
		ResponseEntity<List<Tarefa>> responsePesquisa = template.exchange(getUrlBase()  + "?nome=" + response.getBody().getNome() + "&status=" + response.getBody().getStatus(), HttpMethod.GET, request,
				new ParameterizedTypeReference<List<Tarefa>>() {
				});
		
		assertEquals(HttpStatus.SC_OK, responsePesquisa.getStatusCodeValue());
		logger.info(response.getBody().toString());
		
		
	
	}
}
