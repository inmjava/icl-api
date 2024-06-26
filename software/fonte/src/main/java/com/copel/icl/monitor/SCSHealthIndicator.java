package com.copel.icl.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.copel.icl.externo.scs.SCSRest;

@Component
public class SCSHealthIndicator implements HealthIndicator {
    /** Atributo para Log {@link Logger} */
	private static Logger logger = LoggerFactory.getLogger(SCSHealthIndicator.class);
    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    @Autowired
    SCSRest scsRest;
    
    private int check() {
        // perform some specific health check
    	
    	try {
			logger.debug("testando conexao com o SCS");
			String versao = scsRest.getVersaoServico();
			logger.debug("acessando a versao " + versao);
		} catch (Exception e) {
			return 1;
		}

        return 0;
    }

}