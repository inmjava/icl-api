package com.copel.icl.controller;

import java.io.BufferedReader;
import java.io.FileWriter;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.copel.icl.AutenticacaoCastlightHelper;
import com.copel.icl.HttpHelperCastlight;
import com.copel.icl.dto.ActiveEmployeeCastLightDTO;
import com.copel.icl.dto.EmployeeCastLightDTO;
import com.copel.icl.service.ProfissionalService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
@EnableScheduling
public class EnviarDadosCastlight {
	private static final Logger logger = LoggerFactory.getLogger(EnviarDadosCastlight.class);

	@Autowired
	private final ProfissionalService profissionalService;

	private static final String TIME_ZONE = "America/Sao_Paulo";
	private static final Integer BATCH_SIZE = 50;

	public EnviarDadosCastlight(ProfissionalService profissionalService) {
		super();
		this.profissionalService = profissionalService;
	}

	@Scheduled(cron = "0 15,20,25 10,11 * * *", zone = TIME_ZONE)
	public void enviaDadosCastlightMadrugada() {
		logger.info("Iniciando o envio de dados... " + LocalDateTime.now());
		System.out.println(" ......................................................................................................");
//		enviaDados();
		logger.info("Finalizando o envio de dados... " + LocalDateTime.now());
	}

	private void enviaDados() {
		try {

			String bearer = AutenticacaoCastlightHelper.getAutenticacaoCastlight();

			int totalCountEmployeesSent = 0;

			List<EmployeeCastLightDTO> employeesDTO = this.profissionalService.loadEmployees();
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
				logger.info("tamanho lista ativos: " + allActiveEmployeesDTO.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String jsonActiveEmployeesToSend = listOfObjectsToJson(allActiveEmployeesDTO);

			// salvar arquivo apenas para debug/conferencia via postman - pode remover
			String fileName = "d:\\temp\\myList.json";
			try (FileWriter writer = new FileWriter(fileName)) {
				writer.write(jsonActiveEmployeesToSend);
				System.out.println("JSON data saved to " + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}

			sendActiveEmployeesToCastlight(bearer, jsonActiveEmployeesToSend);

		} catch (Exception e) {
			System.out.println(
					"<--------------------------------------------- erro <---------------------------------------------");
			System.out.println(e.getMessage());

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
		HttpResponse httpResponse = HttpHelperCastlight.doPost(bearer,
				"https://copel-dis.api.hmg.castlight.com.br/api/v3/integration/employee", jsonToSend);

		InputStream contentResponse = httpResponse.getEntity().getContent();
		String retorno = readInputStreamAsString(contentResponse);
		logger.info("    retorno: " + retorno);

		logger.info("fim batch POST: " + LocalDateTime.now());
	}

	private void sendActiveEmployeesToCastlight(String bearer, String jsonOnlyActiveEmployee) throws Exception, IOException {

		logger.info("ini inactivate POST: " + LocalDateTime.now() + " enviando...: ");
		HttpResponse httpResponse = HttpHelperCastlight.doPost(bearer,
				"https://copel-dis.api.hmg.castlight.com.br/api/v3/integration/employee/inactivate", jsonOnlyActiveEmployee);

		InputStream contentResponse = httpResponse.getEntity().getContent();
		String retorno = readInputStreamAsString(contentResponse);
		logger.info("    retorno: " + retorno);

		logger.info("fim inactivate POST: " + LocalDateTime.now() + " enviados!");
	}

}