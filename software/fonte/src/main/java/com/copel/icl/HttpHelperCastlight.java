package com.copel.icl;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.copel.icl.util.Ambiente;
import com.copel.icl.util.AmbienteEnum;

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
	
//	public static HttpResponse doPost(String authValue, String url, String jsonPayload) throws Exception {
//		logger.info("doPost em: " + url);
//		HttpPost requestPost = new HttpPost(url);
//
//		
//		requestPost.addHeader("Accept", "application/json");
//		if (authValue != null) {
//			requestPost.addHeader("Authorization", authValue);
//		}
//		if(jsonPayload != null) {
//			StringEntity requestEntity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
//			requestPost.setEntity(requestEntity);
//		}
//		
//		RequestConfig requestConfig = null;
//		try {
//			requestConfig = getRequestConfig();
//			requestPost.setConfig(requestConfig);
//		} catch (Exception e) {
//			logger.error("erro ao configurar o proxy: " + e);
//		}
//				
//				
//		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(requestPost);
//		
//		int returnedHttpCode = httpResponse.getStatusLine().getStatusCode();
//		logger.info("doPost -------------------> httpcode: " + returnedHttpCode);
//		
//		return httpResponse;
//	}
	
	
	public static RequestConfig getRequestConfig(){
		HttpHost proxy = new HttpHost("oseproxy.copel.nt", 3128, "http");
		
		if(Ambiente.getAmbiente() == AmbienteEnum.LOCAL || Ambiente.getAmbiente() == AmbienteEnum.DSV){
			logger.info(" ambiente local nao usar proxy");
			RequestConfig requestConfig = RequestConfig.custom().build();
			return requestConfig;
		}else{
			RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
			return requestConfig;
		}
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
