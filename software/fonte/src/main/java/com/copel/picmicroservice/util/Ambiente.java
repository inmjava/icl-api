package com.copel.picmicroservice.util;

public class Ambiente {

	public static AmbienteEnum getAmbiente() {
		String testeAmbiente = PropriedadesAplicacao.getInstance().getAmbiente();

		return AmbienteEnum.toEnum(testeAmbiente);

	}

}
