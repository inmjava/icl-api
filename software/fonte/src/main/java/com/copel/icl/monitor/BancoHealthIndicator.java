package com.copel.icl.monitor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.copel.icl.dao.ProfissionalDAOImpl;
import com.copel.icl.dto.EmployeeCastLightDTO;
import com.copel.icl.externo.scs.SCSRest;
import com.copel.monitor.pojo.Mensagem;

@Component
public class BancoHealthIndicator implements HealthIndicator {

	private static Logger logger = LoggerFactory.getLogger(BancoHealthIndicator.class);
    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

	@Autowired
	private ProfissionalDAOImpl profissionalDAOImpl;
    
    private int check() {
        // perform some specific health check
    	   	
    	
		try {
			logger.info("iniciando teste de listar empregados...");
			List<EmployeeCastLightDTO> employees;
			employees = this.profissionalDAOImpl.loadEmployees();
			logger.info("fim teste de listar empregados...: qtd: " + employees.size());
			if (employees.size()<1000) {
				throw new Exception("lista com menos de 1000 empregados...");
			}			
		} catch (Exception e) {
			return 1;
		}

        return 0;
    }

}