package com.copel.picmicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoSegurancaBasicTest {
	private static final Logger logger = LoggerFactory.getLogger(DemoSegurancaBasicTest.class);

	private static String basicProduto;
	private static String basicUsuario;
	private static String basicProdutoChaveInvalida;
	
	@LocalServerPort
	private  int port;
	
	private String getUrlBase() {
		return "http://localhost:" + port + "/" + ConstantesTest.CONTEXT+ "/demo/seg/basic";

	}
	
	@BeforeAll
	public static void beforeAll() throws Exception {
		 basicProduto = AutenticacaoHelper.getAutenticacaoBasic(ConstantesTest.chaveProdutoTest, ConstantesTest.senhaChaveProdutoTest);
		 basicUsuario = AutenticacaoHelper.getAutenticacaoBasic(ConstantesTest.chaveUsuarioTest, ConstantesTest.senhaChaveUsuarioTest);
		 basicProdutoChaveInvalida = AutenticacaoHelper.getAutenticacaoBasic("rhjwshXXXXXX", "xxxxxx");
	}

	@Test
	public void quandoDevePertencerAGrupo_comAutenticacaoQuePertence_retorna200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(basicProduto, getUrlBase() + "/devePertencerGrupoHML_GS_WS_RHJ_H");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void quandoDevePertencerAGrupo_comAutenticacaoQueNaoPertence_retorna403() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(basicUsuario, getUrlBase() + "/devePertencerGrupoHML_GS_WS_RHJ_H");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void quandoDevePertencerAGrupoPRD_comAutenticacaoQueNaoPertence_retorna403() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(basicProduto, getUrlBase() + "/devePertencerGrupoPRD_GS_WS_RHJ");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void quandoDevePertencerAGrupoHMLePRD_comAutenticacaoQuePertenceAmbienteHML_retorna200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(basicProduto, getUrlBase() + "/devePertencerGrupoHML_GS_WS_RHJ_H_ou_devePertencerGrupoPRD_GS_WS_RHJ");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
	
	@Test
	public void quandoPublico_semAutenticar_retorna200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(null, getUrlBase() + "/publico");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

	}
	
	@Test
	public void quandoPublico_comAutenticacaoCorreta_retorna200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(basicProduto, getUrlBase() + "/publico");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

	}
	
	@Test
	public void quandoPublico_comAutenticacaoErrada_retorna401() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(basicProdutoChaveInvalida, getUrlBase() + "/publico");
		assertEquals(HttpStatus.SC_UNAUTHORIZED, httpResponse.getStatusLine().getStatusCode());

	}

	@Test
	public void quandoNecessarioApenasAutenticar_semAutenticar_retorna401() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(null, getUrlBase() + "/apenas-autenticado");
		assertEquals(HttpStatus.SC_UNAUTHORIZED, httpResponse.getStatusLine().getStatusCode());

	}
	
	@Test
	public void quandoNecessarioApenasAutenticar_comAutenticacaoCorreta_retorna200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(basicProduto, getUrlBase() + "/apenas-autenticado");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

	}
	
	@Test
	public void quandoNecessarioApenasAutenticar_comAutenticacaoErrada_retorna401() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(basicProdutoChaveInvalida, getUrlBase() + "/apenas-autenticado");
		assertEquals(HttpStatus.SC_UNAUTHORIZED, httpResponse.getStatusLine().getStatusCode());

	}

	


}
