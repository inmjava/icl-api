package com.copel.picmicroservice.controller;

public class Information {
	
	private String nome;
	private String dados;
	private Tabela[] tabela;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDados() {
		return dados;
	}
	public void setDados(String dados) {
		this.dados = dados;
	}
	public Tabela[] getTabela() {
		return tabela;
	}
	public void setTabela(Tabela[] tabela) {
		this.tabela = tabela;
	}

}
