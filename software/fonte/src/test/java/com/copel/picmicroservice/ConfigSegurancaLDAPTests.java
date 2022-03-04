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

public class ConfigSegurancaLDAPTests {

	@Autowired
	private MockMvc mockMvc;


	@Test
	@WithMockUser(username = "C025427", roles = "ARCHITECT")
	public void quandoUrlBasicAutenticada_comBasic_http200() throws Exception {
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/basic/user"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	@Test
	public void quandoUrlBasicAutenticada_semBasic_http401() throws Exception {

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/basic/user"))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	@Test
	public void quandoUrlBasicPublica_semBasic_http200() throws Exception {

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/basic/publico"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}
	
	
	@Test
	@WithMockUser(username = "C025427", roles = "ARCHITECT")
	public void quandoUrlBasicPublica_comBasic_http200() throws Exception {

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/demo/seg/basic/publico"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	@Test
	public void quandoUrlLivenessActuator_semBasic_http200() throws Exception {

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health/liveness"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}
	
	@Test
	@WithMockUser(username = "C025427", roles = "ARCHITECT")
	public void quandoUrlLivenessActuator_comBasic_http200() throws Exception {

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health/liveness"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}

	
	
	
	@Test
	public void quandoUrlDeniedActuator_semBasic_http401() throws Exception {

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/actuator/refresh"))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized()).andDo(MockMvcResultHandlers.print())
				.andReturn();

		String body = result.getResponse().getContentAsString();

		System.out.println(body);

	}
	
	

	@Test
	@WithMockUser(username = "C025427", roles = "ARCHITECT")
	public void quandoUrlDeniedActuator_comBasic_http403() throws Exception {

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/actuator/refresh"))
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
