package com.copel.icl.controller;


import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.copel.icl.doc.OpenApi30Config;
import com.copel.icl.dto.EmployeeCastLightDTO;
import com.copel.icl.service.ProfissionalService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@Validated


@RequestMapping("/profissional")
@Tag(name = "Lista profissionais", description = "Controller dos profissionais")
@SecurityRequirement(name = OpenApi30Config.OAUTH2_CODE_NAME)
public class ProfissionalController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProfissionalController.class);
	
	@Autowired
    private final ProfissionalService profissionalService;

	@Autowired
	private final EnviarDadosCastlight enviarDadosCastlight;

    public ProfissionalController(ProfissionalService profissionalService, EnviarDadosCastlight enviarDadosCastlight) {
        this.profissionalService = profissionalService;
		this.enviarDadosCastlight = enviarDadosCastlight;
       
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<EmployeeCastLightDTO>> consulta() {
        var pessoasDTO = this.profissionalService.loadEmployees();        
        return new ResponseEntity<List<EmployeeCastLightDTO>>(pessoasDTO, HttpStatus.OK);
    }
    
    
    @GetMapping(path = "/send-all")
    public void autentica() {
    	try {
    		logger.info("Iniciando send-all... " + LocalDateTime.now());
			enviarDadosCastlight.enviaDadosCastlight();
			logger.info("Finalizando send-all... " + LocalDateTime.now());
		} catch (Exception e) {
			logger.error("erro ao enviar dados manualmente: " + e.getMessage());
			logger.error("erro ao enviar dados manualmente (full): " + e);
		}
    }
        
}
