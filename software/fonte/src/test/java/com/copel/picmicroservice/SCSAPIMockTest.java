package com.copel.picmicroservice;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.copel.icl.externo.scs.SCSRest;
/**
 * Simula uma chamada na api rest do SCS
 * @author C032141
 *
 */

@SpringBootTest
public class SCSAPIMockTest {
	
	@Autowired
	@Qualifier("restTemplateSCS")
	private RestTemplate restTemplate;
	
	@Autowired
	SCSRest scsRest;
	
	private MockRestServiceServer mockServer;
	
	@BeforeEach
	public void init() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
		
		
	}
	
	@Test
	public void testaGetPermissaoPerfil() {
		
		
		String nomePerfil = "P01Mock";
		String sigla = "PIC";
		String chave = "c032141";
		
		String url = "perfil/" + chave + "/" + sigla + "/" + nomePerfil;

		
		   mockServer.expect(once(), 
		   requestTo("http://eap01dsv/SCSWS/rest/scsapi/" + url))
           .andRespond(withSuccess("true", 
        		   MediaType.APPLICATION_JSON));
		   

		boolean acesso = scsRest.possuiPerfil(chave, sigla, nomePerfil);
		
		assertTrue(acesso);
	
		   
		   
	}
	
	

	
	
	
}
