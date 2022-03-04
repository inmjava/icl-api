package com.copel.picmicroservice.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.copel.picmicroservice.doc.OpenApi30Config;
import com.copel.picmicroservice.entidade.Tarefa;
import com.copel.picmicroservice.externo.scs.FuncionalidadeDTO;
import com.copel.picmicroservice.externo.scs.PerfilDTO;
import com.copel.picmicroservice.seguranca.DecisorDeAcesso;

import datadog.trace.api.Trace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@CrossOrigin(origins = {"localhost", "*.copel.com", "*.copel.nt"})
@RestController
@RequestMapping("/proxy")
public class ProxyController {
	
   private static Logger logger = LoggerFactory.getLogger(ProxyController.class);
   
   @Autowired
   private DecisorDeAcesso decisorAcesso;

	@Autowired
	@Qualifier("restTemplateRHJFoto")
	RestTemplate restTemplateRHFoto;
	@Trace
	@RequestMapping("/srh/foto/profissionais/{chave}")
	public ResponseEntity<String> getFoto(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request, HttpServletResponse response,
            @RequestHeader HttpHeaders headers) throws Exception 
	{
        
        String end = request.getRequestURI();
        
        end = end.replace("/pic-api/proxy/srh/foto", "") + "/foto";
		logger.info("recuperando foto :" + end);

        restTemplateRHFoto.setMessageConverters(
               Arrays.asList(new ByteArrayHttpMessageConverter()));
        
	     byte[] imageBytes = restTemplateRHFoto.getForObject(end, byte[].class);	     

	     String encoded = Base64.getEncoder().encodeToString(imageBytes);
	     ResponseEntity<String> retorno = new ResponseEntity<> (encoded,headers,HttpStatus.OK);


	      return retorno;
	}

	
	@Autowired
	@Qualifier("restTemplateRHJ")
	RestTemplate restTemplate;
	@Trace
	@RequestMapping("/srh/dados/**")
	public ResponseEntity<String> proxy(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request, HttpServletResponse response,
            @RequestHeader HttpHeaders headers) throws Exception 
	{
        
        String end = request.getRequestURI();
        
        end = end.replace("/pic-api/proxy/srh/dados", "");
		    
		logger.info("recuperando dados :" + end);

		HttpHeaders hs = new HttpHeaders();
		hs.setContentType(headers.getContentType());
		
		ResponseEntity<String> retorno = restTemplate.exchange(end, method, new HttpEntity<String>(body, hs), String.class);

		hs = new HttpHeaders();
		hs.setContentType(retorno.getHeaders().getContentType());
       
		return new ResponseEntity<String>(retorno.getBody(), hs, retorno.getStatusCode());
	}
	
	
	
	@Autowired
	@Qualifier("restTemplateSCS")
	RestTemplate restTemplateSCS;
	@Trace

	@RequestMapping("/scs/dados/**")
	public ResponseEntity<String> proxySCS(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request, HttpServletResponse response,
            @RequestHeader HttpHeaders headers) throws Exception 
	{
        
        String end = request.getRequestURI();
        
        end = end.replace("/pic-api/proxy/scs/dados", "");
		
        logger.info("chamando -->" + end);
        
        HttpHeaders hs = new HttpHeaders();
		hs.setContentType(headers.getContentType());
		
		ResponseEntity<String> retorno =
	     restTemplateSCS.exchange(end, method, new HttpEntity<String>(body), String.class);
		

	    	      
	    hs = new HttpHeaders();
		hs.setContentType(retorno.getHeaders().getContentType());
	       
	    return new ResponseEntity<String>(retorno.getBody(), hs, retorno.getStatusCode());
		
	}
	
	
	/**
	 * Lista as funcionalidades do usuario logado.
	 * @param params
	 * @return
	 * @throws AppException 
	 */
	@Operation(summary = "Lista as funcionalidades do usuário logado.", security = {@SecurityRequirement(name = OpenApi30Config.OAUTH2_CODE_NAME)})
	@ApiResponses(
			value = {
			  @ApiResponse(responseCode = "200", description = "Lista de funcionalidaes do usuario retornadas com sucesso"),
			  @ApiResponse(responseCode = "401", description = "Sem autenticação para acessar este recurso"),
			  @ApiResponse(responseCode = "403", description = "Sem permissao para acessar este recurso"),
			  @ApiResponse(responseCode = "404", description = "Serviço não encontrado"),
			  @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")}
		)
	@GetMapping(path = "/funcionalidades")
	public ResponseEntity<List<FuncionalidadeDTO>> getFuncionalidadesUsuarioLogado() throws Exception {
		return ResponseEntity.ok(decisorAcesso.getFuncionalidadesUsuario());
	}
	
	/**
	 * Verifica se o usuario tem acesso a funcionalidade informada
	 * @param params
	 * @return
	 * @throws AppException 
	 */
	
	@Operation(summary = "Verifica se o usuário tem acesso a funcionalidade informada.", security = {@SecurityRequirement(name = OpenApi30Config.OAUTH2_CODE_NAME)})
	@GetMapping(path = "/autorizado/{funcionalidade}")
	public ResponseEntity<Boolean> isAutorizado(@PathVariable String funcionalidade) throws Exception {
		return ResponseEntity.ok(decisorAcesso.possuiFuncionalidadeSCS(funcionalidade));
		
	}
	
	@Operation(summary = "Lista os perfis do usuário logado.", security = {@SecurityRequirement(name = OpenApi30Config.OAUTH2_CODE_NAME)})
	@GetMapping(path = "/perfis")
	public ResponseEntity<List<PerfilDTO>> getPerfisUsuarioLogado() throws Exception {
		return ResponseEntity.ok(decisorAcesso.getPerfisUsuario());
		
	}
}