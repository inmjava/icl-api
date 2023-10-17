package com.copel.icl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpHelperCastlight {
	private static final Logger logger = LoggerFactory.getLogger(HttpHelperCastlight.class);
	
	public static HttpResponse doGet(String authValue, String url) throws Exception {
		HttpGet request = new HttpGet(url);
		if (authValue != null) {
			request.addHeader("Authorization", authValue);
		}
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		showResposta(httpResponse);
		return httpResponse;
	}
	
	public static HttpResponse doPost(String authValue, String url, String jsonPayload) throws Exception {
		HttpPost request = new HttpPost(url);
		
		request.addHeader("Accept", "application/json");
		if (authValue != null) {
			request.addHeader("Authorization", authValue);
		}
		if(jsonPayload != null) {
			StringEntity requestEntity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
			request.setEntity(requestEntity);
		}
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		
		int returnedHttpCode = httpResponse.getStatusLine().getStatusCode();
		System.out.println("-------------------> httpcode: " + returnedHttpCode);
		
		//showResposta(httpResponse);
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
