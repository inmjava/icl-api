package com.copel.picmicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class DemoChamadaExternaPropagandoBearerTest {
	private static final Logger logger = LoggerFactory.getLogger(DemoChamadaExternaPropagandoBearerTest.class);

	//@LocalServerPort
	private int port=8080;

	private String getUrlBase() {
		return "http://localhost:" + port + "/" + ConstantesTest.CONTEXT + "/demo/seg/bearer/externo";
	}

	@Test
	public void quandoNecessarioApenasAutenticar_comAutenticacaoCorreta_retorna200() throws Exception {
		String bearer = AutenticacaoHelper.getAutenticacaoKeycloak("rhjwsh", "l5anox37");
		HttpResponse httpResponse = HttpHelper.doGet(bearer, getUrlBase() + "/apenas-autenticado");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

	}

}
