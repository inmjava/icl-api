package com.copel.icl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AutenticacaoCastlightHelper {
	private static final String castlightUrl = "https://copel-dis.api.hmg.castlight.com.br/api/v3/integration/token";

	public static String getAutenticacaoCastlight() throws Exception {

		String autenticacaoCastlight = "";
		HttpPost request = new HttpPost(castlightUrl);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		nvps.add(new BasicNameValuePair("grant_type", "client_credentials"));
		nvps.add(new BasicNameValuePair("client_id", "a3988ba2-71fe-441d-b4ee-439a495511cd"));
		nvps.add(new BasicNameValuePair("client_secret", "QGydS3qwEtukw9rqr6yvfJNWjVX4FLLwe14zn1gn"));

		request.setEntity(new UrlEncodedFormEntity(nvps));		

		HttpResponse httpResponse;
		try {
			httpResponse = HttpClientBuilder.create().build().execute(request);
		} catch (Exception e) {
			String mensagemErro = "erro ao obter token: " + e;
			throw new AppException(mensagemErro);
			
		}

		int returnedHttpCode = httpResponse.getStatusLine().getStatusCode();
		if (returnedHttpCode == HttpStatus.SC_OK) {
			System.out.println("-------------------> httpcode: " + returnedHttpCode);

			String dados = EntityUtils.toString(httpResponse.getEntity());
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap myMap = objectMapper.readValue(dados, HashMap.class);

			String token = (String) myMap.get("access_token");

			autenticacaoCastlight = "Bearer " + token;
			System.out.println("TOKEN: " + autenticacaoCastlight);

		} else if (returnedHttpCode == HttpStatus.SC_UNAUTHORIZED) {
			throw new UserNotAuthorizedException("Usuário ou senha inválidos!");
		} else if (returnedHttpCode == HttpStatus.SC_FORBIDDEN) {
			throw new UserForbiddenException("Usuário não autorizado!");
		} else {
			String mensagemErro = "Erro ao obter token: " + returnedHttpCode + " - " + httpResponse.getStatusLine();
			throw new AppException(mensagemErro);
		}
		
		return autenticacaoCastlight;

	}
}
