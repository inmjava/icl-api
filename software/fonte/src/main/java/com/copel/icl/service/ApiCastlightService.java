package com.copel.icl.service;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiCastlightService {
	
    @Autowired
    @Qualifier("restTemplateAuthCastligth")
    private RestTemplate restTemplateAuth;
	
	
	public ApiCastlightService() {
		super();
	}

	private static final Logger logger = LoggerFactory.getLogger(ApiCastlightService.class);
	
    @Value("${castlight.url.token}") // URL da API para gerar o token
    private String apiUrl;

    @Value("${castlight.auth.clientId}")
    private String clientId;

    @Value("${castlight.auth.clientSecret}")
    private String clientSecret;

//	public String getAutenticacaoCastlight() throws Exception {		
//		
//		String castlightUrl = apiUrl;
//		logger.info("iniciando autenticacao em: " + castlightUrl + " ...");
//				
//		String tokenCastlight = "";
//		HttpPost requestPost = new HttpPost(castlightUrl);
//
//		List<NameValuePair> requestBody = new ArrayList<NameValuePair>();
//
//		requestBody.add(new BasicNameValuePair("grant_type", "client_credentials"));
//		requestBody.add(new BasicNameValuePair("client_id", clientId));
//		requestBody.add(new BasicNameValuePair("client_secret", clientSecret));
//
//		requestPost.setEntity(new UrlEncodedFormEntity(requestBody));		
//
//		
//		RequestConfig requestConfig = null;
//		try {
//			requestConfig = HttpHelperCastlight.getRequestConfig();
//			requestPost.setConfig(requestConfig);
//		} catch (Exception e) {
//			logger.error("erro ao configurar o proxy: " + e);
//		}
//
//		HttpResponse httpResponse;
//		try {
//			httpResponse = HttpClientBuilder.create().build().execute(requestPost);
//		} catch (Exception e) {
//			String mensagemErro = "erro ao obter token: " + e;
//			logger.error(mensagemErro);
//			throw new AppException(mensagemErro);
//			
//		}
//
//		int returnedHttpCode = httpResponse.getStatusLine().getStatusCode();
//		logger.info("returned httpCode: " + returnedHttpCode);
//		if (returnedHttpCode == HttpStatus.SC_OK) {
//			String dados = EntityUtils.toString(httpResponse.getEntity());
//			ObjectMapper objectMapper = new ObjectMapper();
//			HashMap myMap = objectMapper.readValue(dados, HashMap.class);
//
//			String token = (String) myMap.get("access_token");
//
//			tokenCastlight = "Bearer " + token;
//			logger.info("TOKEN: " + tokenCastlight.substring(0, 50) + " ...");			
//
//		} else if (returnedHttpCode == HttpStatus.SC_UNAUTHORIZED) {			
//			String errorUnauthorized = "Usuário ou senha inválidos!";
//			logger.error(errorUnauthorized);
//			throw new UserNotAuthorizedException(errorUnauthorized);
//		} else if (returnedHttpCode == HttpStatus.SC_FORBIDDEN) {
//			String errorForbidden = "Usuário não autorizado!";
//			logger.error(errorForbidden);
//			throw new UserForbiddenException(errorForbidden);
//		} else {
//			String errorMessage = "Erro ao obter token: " + returnedHttpCode + " - " + httpResponse.getStatusLine();
//			logger.error(errorMessage);
//			throw new AppException(errorMessage);
//		}
//		
//		return tokenCastlight;
//
//	}
	
    public String getToken() {
        // request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);

        //request to an API
        ResponseEntity<TokenResponse> response = restTemplateAuth.postForEntity(apiUrl, requestBody, TokenResponse.class);

        if (response.getStatusCodeValue() == HttpStatus.SC_OK) {
            TokenResponse tokenResponse = response.getBody();
            String tokenCastlight = tokenResponse.getAccessToken();
			logger.info("TOKEN: " + tokenCastlight .substring(0, 50) + " ...");
            return tokenCastlight;
        } else {
            throw new RuntimeException("Falha ao obter o token");
        }
    }
    
    
    public ResponseEntity<String> doPost(String authValue, String url, String jsonPayload) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        if (authValue != null) {
            headers.set("Authorization", authValue);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> responseEntity;

        try {
            responseEntity = restTemplateAuth.exchange(url, HttpMethod.POST, requestEntity, String.class);
            System.out.println(requestEntity);
        } catch (Exception e) {
            logger.error("Error while making the POST request: " + e.getMessage());
            throw e;
        }

        return responseEntity;
    }
	
}
