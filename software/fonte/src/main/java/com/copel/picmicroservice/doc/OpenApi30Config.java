package com.copel.picmicroservice.doc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.copel.picmicroservice.util.Ambiente;
import com.copel.picmicroservice.util.AmbienteEnum;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Exemplo de documentação OpenAPI - PIC", version = "v1", contact = @Contact(name = "Equipe de Arquitetura")))
@SecuritySchemes({ 
	
	@SecurityScheme(name = OpenApi30Config.BASIC_NAME, type = SecuritySchemeType.HTTP, scheme = "basic")
	
	/*
	,
	@SecurityScheme(name = OpenApi30Config.OPENIDCONNECT_NAME, type = SecuritySchemeType.OPENIDCONNECT, openIdConnectUrl="https://hml.copel.com/kclweb/realms/master/.well-known/openid-configuration")
	*/
	
	/*
	,
	@SecurityScheme(name = OpenApi30Config.OAUTH2_PWD_NAME, type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(
			password = @OAuthFlow(authorizationUrl = "https://hml.copel.com/kclweb/realms/master/protocol/openid-connect/auth", 
			tokenUrl = "https://hml.copel.com/kclweb/realms/master/protocol/openid-connect/token", 
			refreshUrl = "", scopes = {@OAuthScope(name = "openid")})))
			*/
			
			
	
})
public class OpenApi30Config {
	public static final String BASIC_NAME = "basicAuth";
	//public static final String OPENIDCONNECT_NAME = "openIdConnect";
	//public static final String OAUTH2_PWD_NAME = "oauth2Pwd";
	public static final String OAUTH2_CODE_NAME = "oauth2Code";
	
	
	
	@Value("${keycloak.auth-server-url}/realms/master/.well-known/openid-configuration")
	public String openIdConnectUrl;

	
	@Value("${keycloak.auth-server-url}/realms/master/protocol/openid-connect/auth")
	public String authorizationUrl;
	
	
	@Value("${keycloak.auth-server-url}/realms/master/protocol/openid-connect/token")
	public String tokenUrl;


	@Value("${app.contexto}")
	public String appContexto;
	
	
	
	@Bean
	public OpenAPI config() {
		
//		io.swagger.v3.oas.models.security.SecurityScheme esquemaOpenIdConnect = new io.swagger.v3.oas.models.security.SecurityScheme();
//		esquemaOpenIdConnect.type(Type.OPENIDCONNECT).openIdConnectUrl(openIdConnectUrl);
//		
//		io.swagger.v3.oas.models.security.SecurityScheme esquemaOauth2Pwd = new io.swagger.v3.oas.models.security.SecurityScheme();
//		io.swagger.v3.oas.models.security.OAuthFlows flows = new io.swagger.v3.oas.models.security.OAuthFlows();
//		io.swagger.v3.oas.models.security.OAuthFlow flowPwd = new io.swagger.v3.oas.models.security.OAuthFlow();
//		Scopes scopes = new Scopes();
//		flowPwd.authorizationUrl(authorizationUrl).tokenUrl(tokenUrl).scopes(scopes);
//		flows.setPassword(flowPwd);
//		esquemaOauth2Pwd.type(Type.OAUTH2).flows(flows);
		
		
		io.swagger.v3.oas.models.security.SecurityScheme esquemaOauth2Code = new io.swagger.v3.oas.models.security.SecurityScheme();
		io.swagger.v3.oas.models.security.OAuthFlows flows = new io.swagger.v3.oas.models.security.OAuthFlows();
		io.swagger.v3.oas.models.security.OAuthFlow flowCode = new io.swagger.v3.oas.models.security.OAuthFlow();
		Scopes scopes = new Scopes();
		flowCode.authorizationUrl(authorizationUrl).tokenUrl(tokenUrl).scopes(scopes);
		flows.setAuthorizationCode(flowCode);
		esquemaOauth2Code.type(Type.OAUTH2).flows(flows);
		
		
		
		
		//if (Ambiente.getAmbiente() == AmbienteEnum.HML) 
			
		Server serversItemLocal = new Server();
		serversItemLocal.setUrl("http://localhost:8080" + appContexto);
		
		Server serversItemHml = new Server();
		serversItemHml.setUrl("https://webhml" + appContexto);
		
		Server serversItemPrd = new Server();
		serversItemPrd.setUrl("https://webprd" + appContexto);
		
		
	
		return new OpenAPI().addServersItem(serversItemLocal).addServersItem(serversItemHml).addServersItem(serversItemPrd)
		//return new OpenAPI()
		 .components(
					new Components()
					/*.addSecuritySchemes(OPENIDCONNECT_NAME, esquemaOpenIdConnect).addSecuritySchemes(OAUTH2_PWD_NAME, esquemaOauth2Pwd)*/
					.addSecuritySchemes(OAUTH2_CODE_NAME, esquemaOauth2Code)
					);
					
	}
	
	


}
