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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoChamadaExternaTest {
	private static final Logger logger = LoggerFactory.getLogger(DemoChamadaExternaTest.class);

	static {
		
		 // precisa adicionar os certicados para as chamdas ao servicos internos que usam https
		 System.setProperty("javax.net.ssl.trustStore", "store-copel.jks")	;  
		 System.setProperty("javax.net.ssl.trustStorePassword", "copel123")	;  
			  
	}
	
	@LocalServerPort
	private int port;

	public String getUrlBase() {
		String urlBase =  "http://localhost:"+ port +"/" + ConstantesTest.CONTEXT + "/demo/externo/rhj";
		return urlBase;
	}

	@Test
	public void quandoGetVersao_retornaHttp200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(null, getUrlBase() + "/versao");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

	}

	@Test
	public void quandoGetProfissional_retornaHttp200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(null, getUrlBase() + "/profissional/25427/1");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}

	@Test
	public void quandoGetProfissionalPorNome_retornaHttp200() throws Exception {
		HttpResponse httpResponse = HttpHelper.doGet(null, getUrlBase() + "/profissional/nome/nivaldo");
		assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}

}
