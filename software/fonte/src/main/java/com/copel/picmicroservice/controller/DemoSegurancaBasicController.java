package com.copel.picmicroservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.copel.picmicroservice.doc.OpenApi30Config;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/demo/seg/basic")

@Tag(name = "DemoSegurancaBasicController", description = "Exemplo uso de segurança via basic authentication")
@SecurityRequirement(name = OpenApi30Config.BASIC_NAME)
public class DemoSegurancaBasicController {
	
	
	
  
  @GetMapping(path = "/devePertencerGrupoHML_GS_WS_RHJ_H")
  @PreAuthorize("BASIC_GRP_HML('GS_WS_RHJ_H')")
   public ResponseEntity<String> devePertencerGrupoHML_GS_WS_RHJ_H() {
	  return  ResponseEntity.ok("devePertencerGrupoHML_GS_WS_RHJ_H " + System.currentTimeMillis());
  }
  
  @GetMapping(path = "/devePertencerGrupoPRD_GS_WS_RHJ")
  @PreAuthorize("BASIC_GRP_PRD('GS_WS_RHJ')")
   public ResponseEntity<String> devePertencerGrupoPRD_GS_WS_RHJ() {
	  return  ResponseEntity.ok("devePertencerGrupoPRD_GS_WS_RHJ " + System.currentTimeMillis());
  }
  
  @GetMapping(path = "/devePertencerGrupoHML_GS_WS_RHJ_H_ou_devePertencerGrupoPRD_GS_WS_RHJ")
  @PreAuthorize("BASIC_GRP_HML('GS_WS_RHJ_H') or BASIC_GRP_PRD('GS_WS_RHJ')")
   public ResponseEntity<String> devePertencerGrupoHML_GS_WS_RHJ_H_ou_devePertencerGrupoPRD_GS_WS_RHJ() {
	  return  ResponseEntity.ok("devePertencerGrupoHML_GS_WS_RHJ_H_ou_devePertencerGrupoPRD_GS_WS_RHJ " + System.currentTimeMillis());
  }
  
  @GetMapping(path = "/publico")
   public ResponseEntity<String> publico() {
	  return  ResponseEntity.ok("publico " + System.currentTimeMillis());
  }
  
  @GetMapping(path = "/apenas-autenticado")
  public ResponseEntity<String> autenticado() {
	  return  ResponseEntity.ok("apenas-autenticado " + System.currentTimeMillis());
 }
  
  @GetMapping(path = "/user")
  public ResponseEntity<String> getUser() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		return ResponseEntity.ok(principal.toString());
 }
  
  
}
