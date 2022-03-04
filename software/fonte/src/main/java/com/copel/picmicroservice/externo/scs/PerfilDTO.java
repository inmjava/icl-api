package com.copel.picmicroservice.externo.scs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PerfilDTO implements Serializable{
	
	
	public PerfilDTO(String nome) {
		super();
		this.nome = nome;
	}
	
	public PerfilDTO() {
		super();
		
	}
	
	private String nome = "";
	private List<FuncionalidadeDTO> funcionalidades = new ArrayList<>();
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<FuncionalidadeDTO> getFuncionalidades() {
		return funcionalidades;
	}
	public void setFuncionalidades(List<FuncionalidadeDTO> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}
	public void addFuncionalidade(FuncionalidadeDTO funcionalidade) {
		this.funcionalidades.add(funcionalidade);
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PerfilDTO other = (PerfilDTO) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}
