package com.copel.icl.monitor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.copel.icl.dao.ProfissionalDAOImpl;
import com.copel.icl.dto.EmployeeCastLightDTO;
import com.copel.icl.util.PropriedadesAplicacao;
import com.copel.monitor.Monitor;
import com.copel.monitor.pojo.Mensagem;


public class MonitorImpl implements Monitor {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorImpl.class);
			
	@Autowired
	private ProfissionalDAOImpl profissionalDAOImpl;

	@Override
	public List<Mensagem> execute() throws Exception {
		
		List<Mensagem> listaDeErros = new ArrayList<>();

		//teste - variavel de ambiente
		try {
			testarVariavelDeAmbiente();
		}
		catch (Exception e) {
			Mensagem msg = new Mensagem(500, new String[] {e.getMessage()});
			listaDeErros.add(msg);
		}
		
		try {
			logger.info("iniciando teste de listar empregados...");
			List<EmployeeCastLightDTO> employees;
			employees = this.profissionalDAOImpl.loadEmployees();
			logger.info("fim teste de listar empregados...: qtd: " + employees.size());
			if (employees.size()<1000) {
				throw new Exception("lista com menos de 1000 empregados...");
			}
			
		} catch (Exception e) {
			logger.error("503 - erro ao listar empregados no banco de dados....");
			Mensagem msg = new Mensagem(503, new String[] {e.getMessage()});
			listaDeErros.add(msg);
		}
		
		return listaDeErros;		
	}
	
	
	/**
	 * Teste para verificar se esta configurado no servidor ou na JVM a variavel de ambiente AMBIENTE_SRV
	 * @throws Exception
	 */
	private void testarVariavelDeAmbiente() throws Exception{ 		
		if(PropriedadesAplicacao.getInstance().getAmbiente() == null){ 
			throw new Exception("Nao foi possivel recuperar a variavel de ambiente AMBIENTE_SRV configurada no servidor!");
		}
	}



	
}