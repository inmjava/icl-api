package com.copel.icl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AutenticacaoCastlightHelper {
	private static final String castlightUrl = "https://copel-dis.api.hmg.castlight.com.br/api/v3/integration/token";
	private static final Logger logger = LoggerFactory.getLogger(AutenticacaoCastlightHelper.class);

	public static String getAutenticacaoCastlight() throws Exception {		
		
		HttpHost proxy = new HttpHost("oseproxy.copel.nt", 3128, "http");

		String autenticacaoCastlight = "";
		HttpPost request = new HttpPost(castlightUrl);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		nvps.add(new BasicNameValuePair("grant_type", "client_credentials"));
		nvps.add(new BasicNameValuePair("client_id", "a3988ba2-71fe-441d-b4ee-439a495511cd"));
		nvps.add(new BasicNameValuePair("client_secret", "QGydS3qwEtukw9rqr6yvfJNWjVX4FLLwe14zn1gn"));

		request.setEntity(new UrlEncodedFormEntity(nvps));		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setProxy(proxy)
				.setConnectionRequestTimeout(60000).build();
				
		request.setConfig(requestConfig);

		HttpResponse httpResponse;
		try {
			httpResponse = HttpClientBuilder.create().build().execute(request);
		} catch (Exception e) {
			String mensagemErro = "erro ao obter token: " + e;
			logger.error(mensagemErro);
			throw new AppException(mensagemErro);
			
		}

		int returnedHttpCode = httpResponse.getStatusLine().getStatusCode();
		logger.info("returned httpCode: " + returnedHttpCode);
		if (returnedHttpCode == HttpStatus.SC_OK) {
			String dados = EntityUtils.toString(httpResponse.getEntity());
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap myMap = objectMapper.readValue(dados, HashMap.class);

			String token = (String) myMap.get("access_token");

			autenticacaoCastlight = "Bearer " + token;
			logger.info("TOKEN: " + autenticacaoCastlight.substring(0, 50) + " ...");			

		} else if (returnedHttpCode == HttpStatus.SC_UNAUTHORIZED) {			
			String errorUnauthorized = "Usuário ou senha inválidos!";
			logger.error(errorUnauthorized);
			throw new UserNotAuthorizedException(errorUnauthorized);
		} else if (returnedHttpCode == HttpStatus.SC_FORBIDDEN) {
			String errorForbidden = "Usuário não autorizado!";
			logger.error(errorForbidden);
			throw new UserForbiddenException(errorForbidden);
		} else {
			String errorMessage = "Erro ao obter token: " + returnedHttpCode + " - " + httpResponse.getStatusLine();
			logger.error(errorMessage);
			throw new AppException(errorMessage);
		}
		
		return autenticacaoCastlight;

	}
}
