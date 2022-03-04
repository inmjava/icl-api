package com.copel.picmicroservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.c4_soft.springaddons.security.oauth2.test.annotations.ClaimSet;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OidcStandardClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.StringClaim;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.SecurityContextRequestPostProcessorSupport.TestSecurityContextRepository;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.keycloak.ServletKeycloakAuthUnitTestingSupport;
/**
 * 
 * @author C032141
 * Faz o teste simulando a autenticacao do usuario usando o mockmvc
 *
 */

@SpringBootTest
@AutoConfigureMockMvc
@Import(ServletKeycloakAuthUnitTestingSupport.class)

public class DemoMockMvcTest {

	@Autowired
	private MockMvc mockMvc;
	
	
	@Autowired
	private ServletKeycloakAuthUnitTestingSupport keycloak;

	static Authentication getSecurityContextAuthentication(MockHttpServletRequest req) {
		return TestSecurityContextRepository.getContext(req).getAuthentication();
	}
		
	@Test
	@WithMockKeycloakAuth(oidc = @OidcStandardClaims(email = "flaviomil@copel.com", emailVerified = true, preferredUsername = "c032141"), 
						 privateClaims = @ClaimSet(stringClaims = @StringClaim(name = "description", value = "032141|||3||2201|VOPR|32141")))
	public void testaChamadaNecessarioP01comAutorizacao() throws Exception {

		
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/necessarioP01"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	@Test
	@WithMockKeycloakAuth(oidc = @OidcStandardClaims(email = "flaviomil@copel.com", emailVerified = true, preferredUsername = "c032141"), 
							privateClaims = @ClaimSet(stringClaims = @StringClaim(name = "description", value = "032141|||3||2201|VOPR|32141")))

	public void testaChamadaNecessarioP02SemAutorizacao() throws Exception {
		
		// teste chamando o servico do scs
		
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/necessarioP02"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	@Test
	@WithMockKeycloakAuth(oidc = @OidcStandardClaims(email = "flaviomil@copel.com", emailVerified = true, preferredUsername = "c032141"), 
						privateClaims = @ClaimSet(stringClaims = @StringClaim(name = "description", value = "032141|||3||2201|VOPR|32141")))

	public void testaChamadaQuePreciadeUsarioLogado() throws Exception {
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/user"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	@Test
	public void testaChamadaPublica() throws Exception {
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/publico"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}
	
	@Test
	@WithMockUser(username="vvvvv",roles={"GS_WS_RHJ_H"})
	public void quandoDevePertencerAGrupo_comAutenticacaoQuePertence_retorna200() throws Exception {
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/basic/devePertencerGrupoHML_GS_WS_RHJ_H"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);
	}
	
	
	private void setToken(AccessToken token) {
		token.setPreferredUsername("c032141");
		token.setOtherClaims("description", "032141|||3||2201|VOPR|32141");
	}

	@Test
	public void testaChamadaNecessarioP01comAutorizacaoSemAtotacao() throws Exception {
		final KeycloakAuthenticationToken actual = (KeycloakAuthenticationToken) getSecurityContextAuthentication(
				keycloak.authentication()
						.accessToken(token -> setToken(token) )
						.authorities("TEST_AUTHORITY")
						.postProcessRequest(new MockHttpServletRequest()));

		assertThat(actual.getName()).isEqualTo("c032141");
		assertThat(actual.getAuthorities()).containsExactlyInAnyOrder(new SimpleGrantedAuthority("TEST_AUTHORITY"));
		
		SecurityContextHolder.getContext().setAuthentication(actual);

		
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/necessarioP01"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

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
