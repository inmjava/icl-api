package com.copel.icl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/demo/seg/bearer/externo")
public class DemoChamadaExternaPropagandoBearerController {

	@Autowired
	@Qualifier("restTemplateKeycloak")
	RestTemplate restTemplate;

	@GetMapping(path = "/apenas-autenticado")
	public ResponseEntity<String> autenticado() {
		
		String url = "http://localhost:8080/pic-api/demo/seg/bearer/devePertencerGrupoHML_GS_WS_RHJ_H";
		ResponseEntity<String> resposta = restTemplate.getForEntity(url, String.class);
		return resposta;
	
	}
}
