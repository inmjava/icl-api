package com.copel.picmicroservice;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import com.c4_soft.springaddons.security.oauth2.test.annotations.ClaimSet;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OidcStandardClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.StringClaim;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
/**
 * 
 * @author C032141
 * Faz o teste simulando a autenticacao do usuario usando o mockmvc 
 * e fazendo um mock a chamada do scs
 *
 */

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")

public class DemoMockMvcMockSCS {

	@Autowired
	private MockMvc mockMvc;
	
	@Value("${scs.endpoint}")
	String urlSCS;
	
	@Autowired
	@Qualifier("restTemplateSCS")
	private RestTemplate restTemplate;
	
	private MockRestServiceServer mockServer;
	private ClientHttpRequestFactory originalRequestFactory;
	
	@BeforeEach
	public void init() {
		//troca o objeto factory pelo mock
	    originalRequestFactory = restTemplate.getRequestFactory();
		mockServer = MockRestServiceServer.createServer(restTemplate);


	}
	
    @AfterEach 
	public void close() {
		//retorna o http original
	    restTemplate.setRequestFactory(originalRequestFactory);

	}
	
	
	
	private void setMockSCSCall(String chave, String sigla, String nomePerfil, boolean resposta) {
	
		
		String url = "/perfil/" + chave + "/" + sigla + "/" + nomePerfil;
	
		   mockServer.expect(once(), 
		   requestTo(urlSCS + url))
           .andRespond(withSuccess(Boolean.toString( resposta), 
        		   MediaType.APPLICATION_JSON));
		
	}

	
	
	@Test
	@WithMockKeycloakAuth(oidc = @OidcStandardClaims(email = "flaviomil@copel.com", emailVerified = true, preferredUsername = "c032142"), 
						 privateClaims = @ClaimSet(stringClaims = @StringClaim(name = "description", value = "032142|||3||2201|VOPR|32142")))
	public void testaChamadaNecessarioP01comAutorizacao() throws Exception {


		setMockSCSCall("c032142", "PIC", "P01", true);
		
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/necessarioP01"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	@Test
	@WithMockKeycloakAuth(oidc = @OidcStandardClaims(email = "flaviomil@copel.com", emailVerified = true, preferredUsername = "c032142"), 
							privateClaims = @ClaimSet(stringClaims = @StringClaim(name = "description", value = "032142|||3||2201|VOPR|32141")))

	public void testaChamadaNecessarioP02SemAutorizacao() throws Exception {
		
		setMockSCSCall("c032142", "PIC", "P02", false);
		
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/necessarioP02"))
				.andExpect(MockMvcResultMatchers.status().isForbidden() ).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}
	
	@Test
	public void testaChamadaNecessarioP02SemAutenticacao() throws Exception {
		
		
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/necessarioP02"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}





	@TestConfiguration
	public static class TestConfig {
		@Bean
		public GrantedAuthoritiesMapper authoritiesMapper() {
			return new NullAuthoritiesMapper();

		}

	}

}
