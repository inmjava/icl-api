package com.copel.icl.controller;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.copel.icl.doc.OpenApi30Config;
import com.copel.icl.dto.ActiveEmployeeCastLightDTO;
import com.copel.icl.dto.EmployeeCastLightDTO;
import com.copel.icl.service.ApiCastlightService;
import com.copel.icl.service.ProfissionalService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	
	@Value("${castlight.url.employees}")
	private String castlightUrlEmployees;
	
	@Value("${castlight.url.activeEmployees}")
	private String castlightUrlActiveEmployees;
	
	@Value("${castlight.url.token}")
	private String castlightUrlToken;

	@Autowired
    private final ProfissionalService profissionalService;
	
	@Autowired
	private final ApiCastlightService apiCastlightService;
	
	@Autowired
	private final EnviarDadosCastlight enviarDadosCastlight;

    public ProfissionalController(ProfissionalService profissionalService,ApiCastlightService autenticacaoService,EnviarDadosCastlight enviarDadosCastlight) {
        this.profissionalService = profissionalService;
        this.apiCastlightService = autenticacaoService;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	try {
//    		
//    		int batchSize = 50;
//    		int totalCountEmployeesSent = 0;
//    		
////			String bearer = autenticacaoService.getAutenticacaoCastlight();
//			String bearer = apiCastlightService.getToken();
//			
//			List<EmployeeCastLightDTO> employeesDTO = this.profissionalService.loadEmployees();
//			List<ActiveEmployeeCastLightDTO> allActiveEmployeesDTO = new ArrayList<ActiveEmployeeCastLightDTO>();
//			
//			int batchCountEmployeesToSend = 0;
//			
//			List<EmployeeCastLightDTO> batchEmployeesToSend = new ArrayList<EmployeeCastLightDTO>();
//			
//			for (EmployeeCastLightDTO profissionalCastLightDTO : employeesDTO) {
//				boolean isTheLastEmployee = employeesDTO.indexOf(profissionalCastLightDTO) == (employeesDTO.size() -1);
//				
//				batchEmployeesToSend.add(profissionalCastLightDTO);
//				allActiveEmployeesDTO.add(new ActiveEmployeeCastLightDTO(profissionalCastLightDTO.getCpf()));
//				
//				batchCountEmployeesToSend++;
//				totalCountEmployeesSent++;				
//				
//				if(batchCountEmployeesToSend == batchSize || isTheLastEmployee) {
//					
//					String jsonEmployeesToSend = listOfObjectsToJson(batchEmployeesToSend);
////					sendEmployeesToCastlight(totaresponseBodylCountEmployeesSent, bearer, jsonEmployeesToSend);
//					
//					//clean and reset the batch parameters
//					batchCountEmployeesToSend = 0;
//					batchEmployeesToSend = new ArrayList<EmployeeCastLightDTO>();
//				}
//				if(isTheLastEmployee) {
//					System.out.println("último enviado: " + profissionalCastLightDTO.getName());
//				}
//				
//			}
//			
//			try {
//				System.out.println("tamanho lista ativos: " + allActiveEmployeesDTO.size());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			for (EmployeeCastLightDTO employeeCastLightDTO : batchEmployeesToSend) {
//				
//			}
//			
//			String jsonActiveEmployeesToSend = listOfObjectsToJson(allActiveEmployeesDTO);
//			System.out.println(jsonActiveEmployeesToSend);
//			
//			
//	        String fileName = "d:\\temp\\myList.json";
//
//	        try (FileWriter writer = new FileWriter(fileName)) {
//	            writer.write(jsonActiveEmployeesToSend);
//	            System.out.println("JSON data saved to " + fileName);
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }		
//						
////			sendActiveEmployeesToCastlight(bearer,jsonActiveEmployeesToSend);
//		
//		} catch (Exception e) {
//			 System.out.println("<--------------------------------------------- erro <---------------------------------------------");
//			 System.out.println(e.getMessage());
//			
//		}
    	
    	
    }

	private <T> String listOfObjectsToJson(List<T> list) {
		Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		String jsonEmployeesToSend = gson.toJson(list);
		return jsonEmployeesToSend;
	}

	private void sendEmployeesToCastlight(int totalEmployeesSent, String bearer, String jsonToSend)
			throws Exception, IOException {
		LocalDateTime currentDateTime = LocalDateTime.now();;			  
		System.out.println("ini batch POST: " + currentDateTime + " enviando: " + totalEmployeesSent);				
		ResponseEntity<String> responseEntity = this.apiCastlightService.doPost(bearer, castlightUrlEmployees, jsonToSend);
		currentDateTime = LocalDateTime.now();
		System.out.println("fim batch POST: " + currentDateTime + " enviados: " + totalEmployeesSent);
		
		// Get the response status and body
        HttpStatus httpStatus = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();
        
		logger.info("    retorno: " + httpStatus + " - " + responseBody);
	}
	
	private void sendActiveEmployeesToCastlight(String bearer, String jsonToSend)
			throws Exception, IOException {
		LocalDateTime currentDateTime = LocalDateTime.now();		  
		System.out.println("ini active POST: " + currentDateTime + " enviando...: ");				
		ResponseEntity<String> responseEntity = this.apiCastlightService.doPost(bearer, castlightUrlActiveEmployees, jsonToSend);
		
		currentDateTime = LocalDateTime.now();
		System.out.println("fim batch POST: " + currentDateTime + " enviados!" );
		
		// Get the response status and body
        HttpStatus httpStatus = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();
        
        logger.info("    retorno: " + httpStatus + " - " + responseBody);
	}	
        
}
