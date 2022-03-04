package com.copel.picmicroservice.monitor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.copel.monitor.Monitor;
import com.copel.monitor.pojo.Mensagem;
import com.copel.picmicroservice.dao.TarefaDAO;
import com.copel.picmicroservice.util.Ambiente;
import com.copel.picmicroservice.util.AmbienteEnum;
import com.copel.picmicroservice.util.PropriedadesAplicacao;


public class MonitorImpl implements Monitor {
	

	
	@Autowired
	TarefaDAO tarefaDAO;
	
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
		
	
		
		//teste - de acesso ao DAO
		try {
			tarefaDAO.count();
		}
		catch (Exception e) {
			Mensagem msg = new Mensagem(501, new String[] {e.getMessage()});
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