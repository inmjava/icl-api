package com.copel.icl.seguranca.anotacao;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import com.copel.icl.seguranca.DecisorDeAcesso;
import com.copel.icl.util.Ambiente;
import com.copel.icl.util.AmbienteEnum;
import com.copel.icl.util.SpringContext;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot
		implements MethodSecurityExpressionOperations {

	private Object filterObject;
	private Object returnObject;

	public CustomMethodSecurityExpressionRoot(Authentication authentication) {
		super(authentication);
	}

	public boolean SCS_PERF(String nomePerfil) {
		DecisorDeAcesso decisor = SpringContext.getBean(DecisorDeAcesso.class);
		boolean resposta = decisor.possuiPerfilSCS(getAuthentication(), nomePerfil);
		return resposta;
	}

	public boolean SCS_FUNC(String apelidoFuncionalidade) {
		DecisorDeAcesso decisor = SpringContext.getBean(DecisorDeAcesso.class);
		boolean resposta = decisor.possuiFuncionalidadeSCS(getAuthentication(), apelidoFuncionalidade);
		return resposta;
	}

	public boolean GRP_HML(String nomeGrupoHML) {
		boolean resposta = false;
		// eh ambiente hml?
		if (Ambiente.getAmbiente() != AmbienteEnum.PRD) {
			DecisorDeAcesso decisor = SpringContext.getBean(DecisorDeAcesso.class);
			resposta = decisor.possuiGrupo(getAuthentication(), nomeGrupoHML);
		}
		return resposta;
	}

	public boolean GRP_PRD(String nomeGrupoPRD) {
		boolean resposta = false;
		if (Ambiente.getAmbiente() == AmbienteEnum.PRD) {
			DecisorDeAcesso decisor = SpringContext.getBean(DecisorDeAcesso.class);
			resposta = decisor.possuiGrupo(getAuthentication(), nomeGrupoPRD);
		}
		return resposta;
	}

	public boolean BASIC_GRP_HML(String nomeGrupoHML) { 
		boolean resposta = false;
		// eh ambiente hml?
		if (Ambiente.getAmbiente() != AmbienteEnum.PRD) {
			DecisorDeAcesso decisor = SpringContext.getBean(DecisorDeAcesso.class);
			resposta = decisor.possuiGrupoBasic(getAuthentication(), nomeGrupoHML);
		}
		return resposta;
		
	}
	
	public boolean BASIC_GRP_PRD(String nomeGrupoPRD) { 
		boolean resposta = false;
		// eh ambiente hml?
		if (Ambiente.getAmbiente() == AmbienteEnum.PRD) {
			DecisorDeAcesso decisor = SpringContext.getBean(DecisorDeAcesso.class);
			resposta = decisor.possuiGrupoBasic(getAuthentication(), nomeGrupoPRD);
		}
		return resposta;	
	}
	
	@Override
	public Object getFilterObject() {
		return this.filterObject;
	}

	@Override
	public Object getReturnObject() {
		return this.returnObject;
	}

	@Override
	public Object getThis() {
		return this;
	}

	@Override
	public void setFilterObject(Object obj) {
		this.filterObject = obj;
	}

	@Override
	public void setReturnObject(Object obj) {
		this.returnObject = obj;
	}

}