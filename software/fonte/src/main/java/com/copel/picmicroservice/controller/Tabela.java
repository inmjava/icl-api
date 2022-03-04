package com.copel.picmicroservice.controller;

public class Tabela {

	private String nome;
	private String colunas;
	private String[][] dados;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getColunas() {
		return colunas;
	}
	public void setColunas(String colunas) {
		this.colunas = colunas;
	}
	public String[][] getDados() {
		return dados;
	}
	public void setDados(String[][] dados) {
		this.dados = dados;
	}
	
	
}
