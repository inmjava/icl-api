package com.copel.icl.externo.scs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

//TODO: melhorar a construcao das URIs que possuem path params

@Configuration
public class SCSRest {

	@Autowired
	@Qualifier("restTemplateSCS")
	RestTemplate restTemplate;


	public String getVersaoServico() {
		String url = "/version";	
		String versao = restTemplate.getForObject(url, String.class);
		return versao;
	}

	public List<PerfilDTO> getPerfis(String chave, String sigla) {
		String url = "/perfis/" + chave + "/" + sigla;	
		ResponseEntity<List<PerfilDTO>> perfisResponse = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<PerfilDTO>>() {});
		List<PerfilDTO> perfis = perfisResponse == null ? null : perfisResponse.getBody();
		return perfis;
	}

	public boolean possuiFuncionalidade(String chave, String sigla, String apelidoFuncionalidade) {
		String url = "/funcionalidade/" + chave + "/" + sigla + "/" + apelidoFuncionalidade;	
		String possuiResponse = restTemplate.getForObject(url, String.class);
		boolean possui = Boolean.parseBoolean(possuiResponse.toLowerCase());
		return possui;
	}

	public boolean possuiPerfil(String chave, String sigla, String nomePerfil) {
		String url = "/perfil/" + chave + "/" + sigla + "/" + nomePerfil;
		String possuiResponse = restTemplate.getForObject(url, String.class);
		boolean possui = Boolean.parseBoolean(possuiResponse.toLowerCase());
		return possui;
	}

}
