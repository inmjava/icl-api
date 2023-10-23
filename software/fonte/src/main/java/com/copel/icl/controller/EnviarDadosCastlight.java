package com.copel.icl.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.copel.icl.HttpHelperCastlight;
import com.copel.icl.dto.ActiveEmployeeCastLightDTO;
import com.copel.icl.dto.EmployeeCastLightDTO;
import com.copel.icl.service.ApiCastlightService;
import com.copel.icl.service.ProfissionalService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
@EnableScheduling
public class EnviarDadosCastlight {
	private static final Logger logger = LoggerFactory.getLogger(EnviarDadosCastlight.class);

	@Autowired
	private final ProfissionalService profissionalService;
	
	@Autowired
	private final ApiCastlightService apiCastlightService;

	private static final String TIME_ZONE = "America/Sao_Paulo";
	private static final Integer BATCH_SIZE = 50;
	
	@Value("${castlight.url.employees}")
	private String castlightUrlEmployees;
	
	@Value("${castlight.url.activeEmployees}")
	private String castlightUrlActiveEmployees;
	
	@Value("${castlight.url.token}")
	private String castlightUrlToken;

	public EnviarDadosCastlight(ProfissionalService profissionalService, ApiCastlightService autenticacaoService) {
		super();
		this.profissionalService = profissionalService;
		this.apiCastlightService = autenticacaoService;
	}

	@Scheduled(cron = "0 10 * * * *", zone = TIME_ZONE)
	public void enviaDadosCastlightMadrugada() {
		
		logger.info("Iniciando autenticação... " + LocalDateTime.now());
		String bearer = null;
		Boolean isAutenticado = false;		
		try {		
//			bearer = this.autenticacaoService.getAutenticacaoCastlight();		
			bearer = this.apiCastlightService.getToken();
			logger.info("bearer para envio dos batchs: " + bearer.substring(0,50) + " ...");
			isAutenticado = true;
		} catch (Exception e) {			
			logger.error("Error ao autenticar na api: " + e);
			isAutenticado = true;
		}
		logger.info("Finalizando autenticação... " + LocalDateTime.now());
		
		if(isAutenticado) {
			logger.info("Iniciando o envio de dados... " + LocalDateTime.now());
			enviaDados(bearer);
			logger.info("Finalizando o envio de dados... " + LocalDateTime.now());
		}					
	}

	private void enviaDados(String bearer) {
		try {		

			int totalCountEmployeesSent = 0;

			List<EmployeeCastLightDTO> employeesDTO = this.profissionalService.loadEmployees();
			logger.info("Quantidade de empregados encontrados: " + employeesDTO.size());
			
			List<ActiveEmployeeCastLightDTO> allActiveEmployeesDTO = new ArrayList<ActiveEmployeeCastLightDTO>();

			int batchCountEmployeesToSend = 0;

			List<EmployeeCastLightDTO> batchEmployeesToSend = new ArrayList<EmployeeCastLightDTO>();

			for (EmployeeCastLightDTO profissionalCastLightDTO : employeesDTO) {
				boolean isTheLastEmployee = employeesDTO.indexOf(profissionalCastLightDTO) == (employeesDTO.size() - 1);

				batchEmployeesToSend.add(profissionalCastLightDTO);
				allActiveEmployeesDTO.add(new ActiveEmployeeCastLightDTO(profissionalCastLightDTO.getCpf()));

				batchCountEmployeesToSend++;
				totalCountEmployeesSent++;

				if (batchCountEmployeesToSend == BATCH_SIZE || isTheLastEmployee) {

					String jsonEmployeesToSend = listOfObjectsToJson(batchEmployeesToSend);
					logger.info("Inicio enviando para o castlight - quantidade atual: " + totalCountEmployeesSent);
					sendEmployeesToCastlight(bearer, jsonEmployeesToSend);
					logger.info("Fim enviando para o castlight - quantidade atual: " + totalCountEmployeesSent);

					// clean and reset the batch parameters
					batchCountEmployeesToSend = 0;
					batchEmployeesToSend = new ArrayList<EmployeeCastLightDTO>();
				}
				if (isTheLastEmployee) {
					logger.info("último enviado: " + profissionalCastLightDTO.getName());
				}

			}

			try {
				logger.info("Quantidade da lista ativos: " + allActiveEmployeesDTO.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String jsonActiveEmployeesToSend = listOfObjectsToJson(allActiveEmployeesDTO);

			sendActiveEmployeesToCastlight(bearer, jsonActiveEmployeesToSend);

		} catch (Exception e) {
			logger.error("erro ao enviar dados: " + e);
		}
	}

	private static String readInputStreamAsString(InputStream inputStream) throws Exception {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line).append(System.lineSeparator());
			}
			return stringBuilder.toString();
		}
	}

	private <T> String listOfObjectsToJson(List<T> list) {
		Gson gson = new GsonBuilder().serializeNulls()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		String jsonEmployeesToSend = gson.toJson(list);
		return jsonEmployeesToSend;
	}

	private void sendEmployeesToCastlight(String bearer, String jsonToSend) throws Exception, IOException {

		logger.info("ini batch POST: " + LocalDateTime.now());
//		HttpResponse httpResponse = HttpHelperCastlight.doPost(bearer,
//				castlightUrlEmployees, jsonToSend);
		
		ResponseEntity<String> responseEntity = this.apiCastlightService.doPost(bearer, castlightUrlEmployees, jsonToSend);
		

		// Get the response status and body
        HttpStatus httpStatus = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();
        
//		String retorno = readInputStreamAsString(contentResponse);
		logger.info("    retorno: " + responseBody);

		logger.info("fim batch POST: " + LocalDateTime.now());
	}

	private void sendActiveEmployeesToCastlight(String bearer, String jsonOnlyActiveEmployee) throws Exception, IOException {

		logger.info("ini inactivate POST: " + LocalDateTime.now() + " enviando...: ");
//		HttpResponse httpResponse = HttpHelperCastlight.doPost(bearer,
//				castlightUrlActiveEmployees, jsonOnlyActiveEmployee);

//		InputStream contentResponse = httpResponse.getEntity().getContent();
		
		
		ResponseEntity<String> responseEntity = this.apiCastlightService.doPost(bearer, castlightUrlActiveEmployees, jsonOnlyActiveEmployee);
		

		// Get the response status and body
        HttpStatus httpStatus = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();
        
//		String retorno = readInputStreamAsString(contentResponse);
		logger.info("    retorno: " + responseBody);

		logger.info("fim inactivate POST: " + LocalDateTime.now() + " enviados!");
	}

}