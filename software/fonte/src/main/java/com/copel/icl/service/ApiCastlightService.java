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
        } catch (Exception e) {
            logger.error("Error while making the POST request: " + e.getMessage());
            throw e;
        }

        return responseEntity;
    }
	
}
