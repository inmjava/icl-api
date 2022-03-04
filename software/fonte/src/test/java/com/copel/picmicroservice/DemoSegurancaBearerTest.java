package com.copel.picmicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoSegurancaBearerTest {
	private static final Logger logger = LoggerFactory.getLogger(DemoSegurancaBearerTest.class);

	
	private static String bearerProduto;
	private static String bearerUsuario;
	private static String basicProdutoChaveInvalida;
	
	@LocalServerPort
	private  int port;
	
	private String getUrlBase() {
		return "http://localhost:" + port + "/" + ConstantesTest.CONTEXT+ "/demo/seg/bearer";

	}
	
	
	@BeforeAll
	public static void beforeAll() throws Exception {
		 bearerProduto = AutenticacaoHelper.getAutenticacaoKeycloak(ConstantesTest.chaveProdutoTest, ConstantesTest.senhaChaveProdutoTest);
		 bearerUsuario = AutenticacaoHelper.getAutenticacaoKeycloak(ConstantesTest.chaveUsuarioTest, ConstantesTest.senhaChaveUsuarioTest);
		 basicProdutoChaveInvalida = AutenticacaoHelper.getAutenticacaoBasic("rhjwshXXXXXX", "xxxxxx");
	}

	@Test
	public void quandoN_comTokenChaveProduto() throws Exception {

		

		HttpResponse httpResponse = HttpHelper.doGet(bearerProduto, getUrlBase() + "/necessarioP01");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerProduto, getUrlBase() + "/necessarioP02");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerProduto, getUrlBase() + "/necessarioFunc01");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerProduto, getUrlBase() + "/necessarioFunc02");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerProduto, getUrlBase() + "/devePertencerGrupoHML_GS_WS_RHJ_H");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerProduto, getUrlBase() + "/devePertencerGrupoPRD_GS_WS_RHJ");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());
	}

	@Test
	public void quandoN_comTokenChaveUsuario() throws Exception {

		// Necessario chave que possua perfil P01 e funcionalidade Func01 na sigla PIC pelo SCSDSV (http://eap01dsv/scsadm).
		
		HttpResponse httpResponse = HttpHelper.doGet(bearerUsuario, getUrlBase() + "/necessarioP01");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerUsuario, getUrlBase() + "/necessarioP02");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerUsuario, getUrlBase() + "/necessarioFunc01");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerUsuario, getUrlBase() + "/necessarioFunc02");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerUsuario, getUrlBase() + "/devePertencerGrupoHML_GS_WS_RHJ_H");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());

		httpResponse = HttpHelper.doGet(bearerUsuario, getUrlBase() + "/devePertencerGrupoPRD_GS_WS_RHJ");
		assertEquals(HttpStatus.SC_FORBIDDEN, httpResponse.getStatusLine().getStatusCode());

	}

	
	
	@Test
	public void quandoPublico_semAutenticar_retorna200() throws Exception {

		HttpResponse httpResponse = HttpHelper.doGet(null, getUrlBase() + "/publico");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

	}
	
	@Test
	public void quandoPublico_comAutenticacaoCorreta_retorna200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(bearerProduto, getUrlBase() + "/publico");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

	}
	
	@Test
	public void quandoPublico_comAutenticacaoErrada_retorna401() throws Exception {
		String bearer = "Bearer xxxxxxxxxxxxxxxxxxxx";
		HttpResponse httpResponse = HttpHelper.doGet(bearer, getUrlBase() + "/publico");
		assertEquals(HttpStatus.SC_UNAUTHORIZED, httpResponse.getStatusLine().getStatusCode());

	}

	@Test
	public void quandoNecessarioApenasAutenticar_semAutenticar_retorna401() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(null, getUrlBase() + "/apenas-autenticado");
		assertEquals(HttpStatus.SC_UNAUTHORIZED, httpResponse.getStatusLine().getStatusCode());

	}
	
	@Test
	public void quandoNecessarioApenasAutenticar_comAutenticacaoCorreta_retorna200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(bearerProduto, getUrlBase() + "/apenas-autenticado");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

	}
	
	@Test
	public void quandoNecessarioApenasAutenticar_comAutenticacaoErrada_retorna401() throws Exception {
		String bearer = "Bearer xxxxxxxxxxxxxxxxxxxx";
		HttpResponse httpResponse = HttpHelper.doGet(bearer, getUrlBase() + "/apenas-autenticado");
		assertEquals(HttpStatus.SC_UNAUTHORIZED, httpResponse.getStatusLine().getStatusCode());

	}

	
	
	

	

}
