package com.copel.icl;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHelperCastlight {
	private static final Logger logger = LoggerFactory.getLogger(HttpHelperCastlight.class);
	
	public static HttpResponse doGet(String authValue, String url) throws Exception {
		logger.info("doGet em: " + url);
		HttpGet request = new HttpGet(url);
		if (authValue != null) {
			request.addHeader("Authorization", authValue);
		}
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		showResposta(httpResponse);
		return httpResponse;
	}
	
	public static HttpResponse doPost(String authValue, String url, String jsonPayload) throws Exception {
		logger.info("doPost em: " + url);
		HttpPost request = new HttpPost(url);
		HttpHost proxy = new HttpHost("oseproxy.copel.nt", 3128, "http");
		
		request.addHeader("Accept", "application/json");
		if (authValue != null) {
			request.addHeader("Authorization", authValue);
		}
		if(jsonPayload != null) {
			StringEntity requestEntity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
			request.setEntity(requestEntity);
		}
		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setProxy(proxy)
				.setConnectionRequestTimeout(60000).build();
				
		request.setConfig(requestConfig);
		
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		
		int returnedHttpCode = httpResponse.getStatusLine().getStatusCode();
		logger.info("doPost -------------------> httpcode: " + returnedHttpCode);
		
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
