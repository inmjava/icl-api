package com.copel.picmicroservice.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="TAREFA")
@SequenceGenerator(name = "GEN_TAREFA", sequenceName = "ID_TAREFA", allocationSize = 1)
public class Tarefa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_TAREFA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_TAREFA")
	private Long id;
	
	@Size(min = 1, max = 50, message = "Nome deve ter entre 1 a 50 caracteres")
	private String nome;

	@Size(min = 1, max = 10, message = "Status deve ter entre 1 a 10 caracteres")
	private String status;
	
	public Tarefa() {
		
	}

	public Tarefa (String nome, String status) {
		this.nome = nome;
		this.status = status;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("Tarefa[id=%d, nome='%s', status='%s']", id, nome, status);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Tarefa other = (Tarefa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

}
