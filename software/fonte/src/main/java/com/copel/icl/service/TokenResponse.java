package com.copel.icl.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
	@JsonProperty("access_token")
    private String accessToken;
	@JsonProperty("token_type")
    private String tokenType;
	@JsonProperty("expires_in")
    private Long expiresIn;

    // Construtores, getters e setters

    // Exemplo de construtor e métodos getters e setters
    public TokenResponse() {
    }

    public TokenResponse(String accessToken, String tokenType, Long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
