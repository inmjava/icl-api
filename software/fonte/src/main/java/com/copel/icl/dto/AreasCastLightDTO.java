package com.copel.icl.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AreasCastLightDTO implements Serializable {

	private String title;
    private BigDecimal instructureLevel;
    private String instructureTitle;

    	
    public AreasCastLightDTO(Object[] objects) {    	
		super();
		this.title = (String) objects[0];
		this.instructureLevel = (BigDecimal) objects[1];
		this.instructureTitle = (String) objects[2];
	}

    public AreasCastLightDTO(String titleArea, BigDecimal instructureLevelArea, String instructureTitleArea) {
		super();
		this.title = titleArea;
		this.instructureLevel = instructureLevelArea;
		this.instructureTitle = instructureTitleArea;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getInstructureLevel() {
        return instructureLevel;
    }

    public void setInstructureLevel(BigDecimal instructureLevel) {
        this.instructureLevel = instructureLevel;
    }

    public String getInstructureTitle() {
        return instructureTitle;
    }

    public void setInstructureTitle(String instructureTitle) {
        this.instructureTitle = instructureTitle;
    }

    
}
