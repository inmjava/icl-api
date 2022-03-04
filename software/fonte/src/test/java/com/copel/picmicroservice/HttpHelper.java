package com.copel.picmicroservice;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHelper {
	private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);
	
	public static HttpResponse doGet(String authValue, String url) throws Exception {
		HttpGet request = new HttpGet(url);
		if (authValue != null) {
			request.addHeader("Authorization", authValue);
		}
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		showResposta(httpResponse);
		return httpResponse;
	}

	public static void showResposta(HttpResponse httpResponse) throws Exception {
		logger.info("-----------------------------------------------");
		showHeaders(httpResponse);
		showContent(httpResponse);

	}

	public static void showContent(HttpResponse httpResponse) throws ParseException, IOException {
		String dados = getEntityAsString(httpResponse);
		logger.info(dados);

	}

	public static void showHeaders(HttpResponse httpResponse) throws ParseException, IOException {
		for (Header h : httpResponse.getAllHeaders()) {
			logger.info(h.getName() + ":  " + h.getValue());
		}
	}
	
	public static String getEntityAsString(HttpResponse httpResponse) throws ParseException, IOException {
		String dados = EntityUtils.toString(httpResponse.getEntity());
		return dados;

	}

}
