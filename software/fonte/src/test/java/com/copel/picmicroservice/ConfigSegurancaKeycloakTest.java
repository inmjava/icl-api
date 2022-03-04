package com.copel.picmicroservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.c4_soft.springaddons.security.oauth2.test.annotations.ClaimSet;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OidcStandardClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.StringClaim;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")

public class ConfigSegurancaKeycloakTest {

	@Autowired
	private MockMvc mockMvc;


	

	@Test
	@WithMockKeycloakAuth(oidc = @OidcStandardClaims(email = "flaviomil@copel.com", emailVerified = true, preferredUsername = "c032142"), privateClaims = @ClaimSet(stringClaims = @StringClaim(name = "description", value = "032142|||3||2201|VOPR|32142")))
	public void quandoUrlBearerAutenticada_comToken_http200() throws Exception {
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/user"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	@Test
	public void quandoUrlBearerAutenticada_semToken_http401() throws Exception {

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/user"))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	@Test
	public void quandoUrlBearerPublica_semToken_http200() throws Exception {

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/bearer/publico"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
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
