package com.copel.picmicroservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.copel.picmicroservice.util.Ambiente;
import com.copel.picmicroservice.util.PropriedadesAplicacao;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication
public class PicMicroserviceApplication {

	private static final Logger logger = LoggerFactory.getLogger(PicMicroserviceApplication.class);
	
	
	public static void main(String[] args) {
		 // precisa adicionar os certicados para as chamdas ao servicos internos que usam https
		  System.setProperty("javax.net.ssl.trustStore", "/tmp/copel-ose-truststore.jks")	;  
		  System.setProperty("javax.net.ssl.trustStorePassword", "changeit")	;  
			  
		ApplicationContext ctx = SpringApplication.run(PicMicroserviceApplication.class, args);
		
		System.out.println("===>" + PropriedadesAplicacao.getInstance());
		System.out.println("===>" + Ambiente.getAmbiente());
		
		logger.info("APP INICIADA");
	}
	
}
