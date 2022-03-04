package com.copel.picmicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoSegurancaBasicRestTemplateTest {
	private static final Logger logger = LoggerFactory.getLogger(DemoSegurancaBasicRestTemplateTest.class);

	@LocalServerPort
	private  int port;
	
	
	private static String basicProduto;
	private static String basicUsuario;
	private static String basicProdutoChaveInvalida;


	@Autowired
	private TestRestTemplate  restTemplate;
	
	@BeforeAll
	public static void beforeAll() throws Exception {
		 basicProduto = AutenticacaoHelper.getAutenticacaoBasic(ConstantesTest.chaveProdutoTest, ConstantesTest.senhaChaveProdutoTest);
		 basicUsuario = AutenticacaoHelper.getAutenticacaoBasic(ConstantesTest.chaveUsuarioTest, ConstantesTest.senhaChaveUsuarioTest);
		 basicProdutoChaveInvalida = AutenticacaoHelper.getAutenticacaoBasic("rhjwshXXXXXX", "xxxxxx");
	}
	
	private String getUrlBase() {
		  String urlBase = "http://localhost:" + port + "/" + ConstantesTest.CONTEXT+ "/demo/seg/basic";
		  return urlBase;
	}

	@Test
	public void quandoDevePertencerAGrupo_comAutenticacaoQuePertence_retorna200() throws Exception {
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + basicProduto);
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		String url = getUrlBase() + "/devePertencerGrupoHML_GS_WS_RHJ_H";
		logger.info(url);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		String body = response.getBody();
		
		
		//HttpResponse httpResponse = HttpHelper.doGet(basicProduto, urlBase + "/devePertencerGrupoHML_GS_WS_RHJ_H");
		//assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
		assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		
	}
	

	


}
