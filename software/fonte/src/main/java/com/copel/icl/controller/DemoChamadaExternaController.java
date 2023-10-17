package com.copel.icl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.copel.icl.externo.rhj.ProfissionalDTO;

@RestController
@RequestMapping("/demo/externo/rhj")
public class DemoChamadaExternaController {

	@Autowired
	@Qualifier("restTemplateRHJ")
	RestTemplate restTemplate;

	@GetMapping(path = "/versao")
	public ResponseEntity<String> getVersaoServico() {
		String url = "/srhserverversion";
		ResponseEntity<String> versao = restTemplate.getForEntity(url, String.class);
		return versao;
	}

	@GetMapping(path = "/profissional/{numReg}/{tipo}")
	public ResponseEntity<ProfissionalDTO> getProfissionalPorRegistroETipo(@PathVariable("numReg") Long numReg, @PathVariable("tipo") Long tipo) {
		String url = "/profissionais/" + numReg + "/" + tipo;
		ResponseEntity<ProfissionalDTO> response = restTemplate.getForEntity(url, ProfissionalDTO.class);
		return response;
	}
	
	@GetMapping(path = "/profissional/nome/{nome}")
	public ResponseEntity<List<ProfissionalDTO>>  getProfissionalPorNome(@PathVariable("nome") String nome) {
		String url = "/profissionais/nome/" + nome;
		ResponseEntity<List<ProfissionalDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProfissionalDTO>>() {});
		return response;
	}
}
