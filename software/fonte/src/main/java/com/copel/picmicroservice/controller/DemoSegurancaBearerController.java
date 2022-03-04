package com.copel.picmicroservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.copel.picmicroservice.doc.OpenApi30Config;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/demo/seg/bearer")

@Tag(name = "DemoSegurancaBearerController", description = "Exemplo uso de segurança via token")
@SecurityRequirement(name = OpenApi30Config.OAUTH2_CODE_NAME)
public class DemoSegurancaBearerController {

	
	@GetMapping(path = "/user")
	public ResponseEntity<String> getUser() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		return ResponseEntity.ok(principal.toString());
	}
	
	@GetMapping(path = "/necessarioP01")
	@PreAuthorize("SCS_PERF('P01')")
	public ResponseEntity<String> necessarioP01() {
		return ResponseEntity.ok("necessarioP01 " + System.currentTimeMillis());
	}

	@GetMapping(path = "/necessarioP02")
	@PreAuthorize("SCS_PERF('P02')")
	public ResponseEntity<String> necessarioP02() {
		return ResponseEntity.ok("necessarioP02 " + System.currentTimeMillis());
	}

	@GetMapping(path = "/necessarioFunc01")
	@PreAuthorize("SCS_FUNC('func01')")
	public ResponseEntity<String> necessarioFunc01() {
		return ResponseEntity.ok("necessarioFunc01 " + System.currentTimeMillis());
	}

	@GetMapping(path = "/necessarioFunc02")
	@PreAuthorize("SCS_FUNC('func02')")
	public ResponseEntity<String> necessarioFunc02() {
		return ResponseEntity.ok("necessarioFunc02 " + System.currentTimeMillis());
	}

	@GetMapping(path = "/devePertencerGrupoHML_GS_WS_RHJ_H")
	@PreAuthorize("GRP_HML('GS_WS_RHJ_H')")
	public ResponseEntity<String> devePertencerGrupoHML_GS_WS_RHJ_H() {
		return ResponseEntity.ok("devePertencerGrupoHML_GS_WS_RHJ_H" + System.currentTimeMillis());
	}

	@GetMapping(path = "/devePertencerGrupoPRD_GS_WS_RHJ")
	@PreAuthorize("GRP_PRD('GS_WS_RHJ')")
	public ResponseEntity<String> devePertencerGrupoPRD_GS_WS_RHJ() {
		return ResponseEntity.ok("devePertencerGrupoPRD_GS_WS_RHJ" + System.currentTimeMillis());
	}

	@GetMapping(path = "/publico")
	public ResponseEntity<String> publico() {
		return ResponseEntity.ok("publico " + System.currentTimeMillis());
	}

	@GetMapping(path = "/apenas-autenticado")
	public ResponseEntity<String> autenticado() {
		return ResponseEntity.ok("apenas-autenticado " + System.currentTimeMillis());
	}
	
	
	
	
	
}
