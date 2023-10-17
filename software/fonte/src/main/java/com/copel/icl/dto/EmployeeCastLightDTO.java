package com.copel.icl.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EmployeeCastLightDTO implements Serializable {

	private String name;
    private String cpf;
    private String email;
    private String contractType;
    private String position;
    private String phoneNumber;
    private String admissionDate;
    private String resignationDate;    
    private String regional;
    private String businessUnit;
    private BigDecimal registration;
    
    private transient BigDecimal cpfGerenteImediato;
    private transient BigDecimal cpfSupervisor;
    
    private transient BigDecimal cpfDiretor1;
    private transient BigDecimal cpfDiretor2;
    
    private transient String titleArea1;
    private transient BigDecimal instructureLevelArea1;
    private transient String instructureTitleArea1;
    
    private transient String titleArea2;
    private transient BigDecimal instructureLevelArea2;
    private transient String instructureTitleArea2;
    
    private transient String titleArea3;
    private transient BigDecimal instructureLevelArea3;
    private transient String instructureTitleArea3;
    
    private transient String titleArea4;
    private transient BigDecimal instructureLevelArea4;
    private transient String instructureTitleArea4;
    
    private transient String titleArea5;
    private transient BigDecimal instructureLevelArea5;
    private transient String instructureTitleArea5;
    
    private transient String titleArea6;
    private transient BigDecimal instructureLevelArea6;
    private transient String instructureTitleArea6;
    
    private List<String> managers;
    
    private List<AreasCastLightDTO> areas;
    
	public EmployeeCastLightDTO(Object[] objects) {    	
		super();
		
		this.name = (String) objects[0];
		this.cpf = formatCPF((BigDecimal) objects[1]);
		this.email = (String) objects[2];
		this.contractType = (String) objects[3];
		this.position = (String) objects[4];
		this.phoneNumber = (String) objects[5];
		this.admissionDate = (String) objects[6];
		this.resignationDate = (String) objects[7];

		this.regional = (String) objects[8];
		this.businessUnit = (String) objects[9];

		this.registration = (BigDecimal) objects[28];
		
		this.cpfGerenteImediato = (BigDecimal) objects[32];
		this.cpfSupervisor = (BigDecimal) objects[34];
		this.cpfDiretor1 = (BigDecimal) objects[30];
		this.cpfDiretor2 = (BigDecimal) objects[31];
		
		this.titleArea1 = (String) objects[10];
		this.instructureLevelArea1 = (BigDecimal) objects[11];
		this.instructureTitleArea1 = (String) objects[12];

		this.titleArea2 = (String) objects[13];
		this.instructureLevelArea2 = (BigDecimal) objects[14];
		this.instructureTitleArea2 = (String) objects[15];
		
		this.titleArea3 = (String) objects[16];
		this.instructureLevelArea3 = (BigDecimal) objects[17];
		this.instructureTitleArea3 = (String) objects[18];
		
		this.titleArea4 = (String) objects[19];
		this.instructureLevelArea4 = (BigDecimal) objects[20];
		this.instructureTitleArea4 = (String) objects[21];
		
		this.titleArea5 = (String) objects[22];
		this.instructureLevelArea5 = (BigDecimal) objects[23];
		this.instructureTitleArea5 = (String) objects[24];
		
		this.titleArea6 = (String) objects[25];
		this.instructureLevelArea6 = (BigDecimal) objects[26];
		this.instructureTitleArea6 = (String) objects[27];
		
		this.areas = new ArrayList<>();
		if(!isNullOrEmpty(titleArea1)) {
			AreasCastLightDTO area1 = new AreasCastLightDTO(titleArea1,instructureLevelArea1,instructureTitleArea1);
			this.areas.add(area1);
		}	
		
		if(!isNullOrEmpty(titleArea2)) {
			AreasCastLightDTO area2 = new AreasCastLightDTO(titleArea2,instructureLevelArea2,instructureTitleArea2);
			this.areas.add(area2);
		}
		
		if(!isNullOrEmpty(titleArea3)) {
			AreasCastLightDTO area3 = new AreasCastLightDTO(titleArea3,instructureLevelArea3,instructureTitleArea3);
			this.areas.add(area3);			
		}

		if(!isNullOrEmpty(titleArea4)) {
			AreasCastLightDTO area4 = new AreasCastLightDTO(titleArea4,instructureLevelArea4,instructureTitleArea4);
			this.areas.add(area4);			
		}		

		if(!isNullOrEmpty(titleArea5)) {
			AreasCastLightDTO area5 = new AreasCastLightDTO(titleArea5,instructureLevelArea5,instructureTitleArea5);
			this.areas.add(area5);					
		}

		if(!isNullOrEmpty(titleArea6)) {
			AreasCastLightDTO area6 = new AreasCastLightDTO(titleArea6,instructureLevelArea6,instructureTitleArea6);
			this.areas.add(area6);					
		}

		
		this.managers = new ArrayList<>();
		if(this.cpfGerenteImediato != null) {
			this.managers.add(formatCPF(this.cpfGerenteImediato));	
		}
				
		if(this.cpfSupervisor != null) {
			this.managers.add(formatCPF(this.cpfSupervisor));	
		}
		
		if(position.toUpperCase().contains("SUPERINTENDENTE")) {
			this.managers.add(formatCPF(this.cpfDiretor1));	
			this.managers.add(formatCPF(this.cpfDiretor2));			
		}
		
		
	}
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(String resignationDate) {
        this.resignationDate = resignationDate;
    }

    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }
    
    public BigDecimal getRegistration() {
        return registration;
    }

    public void setRegistration(BigDecimal registration) {
        this.registration = registration;
    }
    public BigDecimal getCpfGerenteImediato() {
		return cpfGerenteImediato;
	}

	public void setCpfGerenteImediato(BigDecimal cpfGerenteImediato) {
		this.cpfGerenteImediato = cpfGerenteImediato;
	}

	public BigDecimal getCpfSupervisor() {
		return cpfSupervisor;
	}

	public void setCpfSupervisor(BigDecimal cpfSupervisor) {
		this.cpfSupervisor = cpfSupervisor;
	}

	public BigDecimal getCpfDiretor1() {
		return cpfDiretor1;
	}

	public void setCpfDiretor1(BigDecimal cpfDiretor1) {
		this.cpfDiretor1 = cpfDiretor1;
	}

	public BigDecimal getCpfDiretor2() {
		return cpfDiretor2;
	}

	public void setCpfDiretor2(BigDecimal cpfDiretor2) {
		this.cpfDiretor2 = cpfDiretor2;
	}

	public String getTitleArea1() {
		return titleArea1;
	}

	public void setTitleArea1(String titleArea1) {
		this.titleArea1 = titleArea1;
	}

	public BigDecimal getInstructureLevelArea1() {
		return instructureLevelArea1;
	}

	public void setInstructureLevelArea1(BigDecimal instructureLevelArea1) {
		this.instructureLevelArea1 = instructureLevelArea1;
	}

	public String getInstructureTitleArea1() {
		return instructureTitleArea1;
	}

	public void setInstructureTitleArea1(String instructureTitleArea1) {
		this.instructureTitleArea1 = instructureTitleArea1;
	}

	public String getTitleArea2() {
		return titleArea2;
	}

	public void setTitleArea2(String titleArea2) {
		this.titleArea2 = titleArea2;
	}

	public BigDecimal getInstructureLevelArea2() {
		return instructureLevelArea2;
	}

	public void setInstructureLevelArea2(BigDecimal instructureLevelArea2) {
		this.instructureLevelArea2 = instructureLevelArea2;
	}

	public String getInstructureTitleArea2() {
		return instructureTitleArea2;
	}

	public void setInstructureTitleArea2(String instructureTitleArea2) {
		this.instructureTitleArea2 = instructureTitleArea2;
	}

	public String getTitleArea3() {
		return titleArea3;
	}

	public void setTitleArea3(String titleArea3) {
		this.titleArea3 = titleArea3;
	}

	public BigDecimal getInstructureLevelArea3() {
		return instructureLevelArea3;
	}

	public void setInstructureLevelArea3(BigDecimal instructureLevelArea3) {
		this.instructureLevelArea3 = instructureLevelArea3;
	}

	public String getInstructureTitleArea3() {
		return instructureTitleArea3;
	}

	public void setInstructureTitleArea3(String instructureTitleArea3) {
		this.instructureTitleArea3 = instructureTitleArea3;
	}

	public String getTitleArea4() {
		return titleArea4;
	}

	public void setTitleArea4(String titleArea4) {
		this.titleArea4 = titleArea4;
	}

	public BigDecimal getInstructureLevelArea4() {
		return instructureLevelArea4;
	}

	public void setInstructureLevelArea4(BigDecimal instructureLevelArea4) {
		this.instructureLevelArea4 = instructureLevelArea4;
	}

	public String getInstructureTitleArea4() {
		return instructureTitleArea4;
	}

	public void setInstructureTitleArea4(String instructureTitleArea4) {
		this.instructureTitleArea4 = instructureTitleArea4;
	}

	public String getTitleArea5() {
		return titleArea5;
	}

	public void setTitleArea5(String titleArea5) {
		this.titleArea5 = titleArea5;
	}

	public BigDecimal getInstructureLevelArea5() {
		return instructureLevelArea5;
	}

	public void setInstructureLevelArea5(BigDecimal instructureLevelArea5) {
		this.instructureLevelArea5 = instructureLevelArea5;
	}

	public String getInstructureTitleArea5() {
		return instructureTitleArea5;
	}

	public void setInstructureTitleArea5(String instructureTitleArea5) {
		this.instructureTitleArea5 = instructureTitleArea5;
	}

	public String getTitleArea6() {
		return titleArea6;
	}

	public void setTitleArea6(String titleArea6) {
		this.titleArea6 = titleArea6;
	}

	public BigDecimal getInstructureLevelArea6() {
		return instructureLevelArea6;
	}

	public void setInstructureLevelArea6(BigDecimal instructureLevelArea6) {
		this.instructureLevelArea6 = instructureLevelArea6;
	}

	public String getInstructureTitleArea6() {
		return instructureTitleArea6;
	}

	public void setInstructureTitleArea6(String instructureTitleArea6) {
		this.instructureTitleArea6 = instructureTitleArea6;
	}

	public List<AreasCastLightDTO> getAreas() {
		return areas;
	}

	public void setAreas(List<AreasCastLightDTO> areas) {
		this.areas = areas;
	}

	public List<String> getManagers() {
		return managers;
	}

	public void setManagers(List<String> managers) {
		this.managers = managers;
	}

    private String formatCPF(BigDecimal number) {
    	if(number != null) {
            String numberString = zeroPadString(number.toString(),11);
            // Insert separators and return the formatted CPF
            return numberString.substring(0, 3) + "." + numberString.substring(3, 6) + "." +
                   numberString.substring(6, 9) + "-" + numberString.substring(9);
    	}else {
    		return null;
    	}

    }
    
    private String zeroPadString(String input, int length) {
        return StringUtils.leftPad(input, length, '0');
    }
    
    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
