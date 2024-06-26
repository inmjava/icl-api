package com.copel.icl.excecao;

public enum ProblemType {

	DADOS_INVALIDOS("Dados inválidos"),
	ERRO_DE_SISTEMA("Erro de sistema"),
	PARAMETRO_INVALIDO( "Parâmetro inválido"),
	MENSAGEM_INCOMPREENSIVEL("Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("Recurso não encontrado"),
	ENTIDADE_EM_USO("Entidade em uso"),
	ERRO_NEGOCIO("Violação de regra de negócio"),
	NAO_AUTORIZADO("Usuário não autorizado."),
	ERRO_INTEGRACAO("Erro de integração");
	
	private String title;
	
	ProblemType(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
}
