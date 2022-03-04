package com.copel.picmicroservice.externo;

import java.time.Duration;

import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigRestTemplate {
	
	@Value("${rhj.endpoint}")
	String urlRHJ;
	
	@Value("${rhj.chave-produto-x}")
	String chaveProdutoRHJ;
	
	@Value("${rhj.senha-chave-produto-x}")
	String senhaChaveProdutoRHJ;
		
	@Value("${rhj.connect-timeout}")
	Long connectTimeoutRHJ;
	
	@Value("${rhj.read-timeout}")
	Long readTimeoutRHJ;
	
	
	@Bean(name="restTemplateRHJ")
	public RestTemplate restTemplateRHJ(RestTemplateBuilder builder) {
		return builder.basicAuthentication(chaveProdutoRHJ, senhaChaveProdutoRHJ).rootUri(urlRHJ)
				.setConnectTimeout(Duration.ofMillis(connectTimeoutRHJ))
				.setReadTimeout(Duration.ofMillis(readTimeoutRHJ)).build();
	}
	
	@Bean(name="restTemplateRHJFoto")
	public RestTemplate restTemplateRHJFoto(RestTemplateBuilder builder) {
		return builder.basicAuthentication(chaveProdutoRHJ, senhaChaveProdutoRHJ).rootUri(urlRHJ)
				.setConnectTimeout(Duration.ofMillis(connectTimeoutRHJ))
				.setReadTimeout(Duration.ofMillis(readTimeoutRHJ)).build();
	}



	
	//-----------------------
	
	@Autowired
	KeycloakClientRequestFactory factory;
	
	@Bean(name="restTemplateKeycloak")
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) 
	public KeycloakRestTemplate restTemplateKeycloak () {
		return new KeycloakRestTemplate(factory);
	}
	
	
	//-----------------------

	@Value("${scs.endpoint}")
	String urlSCS;

	@Value("${scs.chave-produto-x}")
	String chaveProdutoSCS;
	
	@Value("${scs.senha-chave-produto-x}")
	String senhaChaveProdutoSCS;
	
	@Value("${scs.connect-timeout}")
	Long connectTimeoutSCS;
	
	@Value("${scs.read-timeout}")
	Long readTimeoutSCS;
	
	@Bean(name="restTemplateSCS")
	public RestTemplate restTemplateSCS(RestTemplateBuilder builder) {
		return builder.basicAuthentication(chaveProdutoSCS, senhaChaveProdutoSCS).rootUri(urlSCS)
				.setConnectTimeout(Duration.ofMillis(connectTimeoutSCS))
				.setReadTimeout(Duration.ofMillis(readTimeoutSCS)).build();
	}



	
}
