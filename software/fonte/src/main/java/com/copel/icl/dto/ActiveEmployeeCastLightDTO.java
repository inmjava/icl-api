package com.copel.icl.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ActiveEmployeeCastLightDTO implements Serializable {

    private String cpf;
    
	public ActiveEmployeeCastLightDTO(String cpf) {    	
		super();
		this.cpf = cpf;
		
	}
    

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

  
}
