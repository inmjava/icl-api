package com.copel.picmicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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

public class AutenticacaoHelper {
	private static final String keycloakUrl = "https://hml.copel.com/kclweb/realms/master/protocol/openid-connect/token";


	public static String getAutenticacaoBasic(String chave, String senha) throws Exception {
		String auth = chave + ":" + senha;
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
		String resposta = new String(encodedAuth);
		return "Basic " + resposta;

	}
	
	public static String getAutenticacaoKeycloak(String chave, String senha) throws Exception {

		/**
		 * { "realm": "master", "auth-server-url": "https://hml.copel.com/kclweb/",
		 * "ssl-required": "none", "resource": "pic", "verify-token-audience": true,
		 * "credentials": { "secret": "049e9cae-ed10-4917-902e-796db66224b9" },
		 * "use-resource-role-mappings": true, "confidential-port": 0 }
		 */

		//if (autenticacaoKCL == null) {

			HttpPost request = new HttpPost(keycloakUrl);

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			nvps.add(new BasicNameValuePair("client_id", "pic-microservico"));
			nvps.add(new BasicNameValuePair("username", chave));
			nvps.add(new BasicNameValuePair("password", senha));
			nvps.add(new BasicNameValuePair("grant_type", "password"));
			nvps.add(new BasicNameValuePair("client_secret", "e68f7c8f-e782-48cb-93c2-87d1e1d3f2fb"));

//			nvps.add(new BasicNameValuePair("client_id", "pic-microservico"));
//			nvps.add(new BasicNameValuePair("username", "01822392926"));
//			nvps.add(new BasicNameValuePair("password", "Cr1.........1"));
//			nvps.add(new BasicNameValuePair("grant_type", "password"));
//			nvps.add(new BasicNameValuePair("client_secret", "e68f7c8f-e782-48cb-93c2-87d1e1d3f2fb"));

			request.setEntity(new UrlEncodedFormEntity(nvps));

			final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

			assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

			String dados = EntityUtils.toString(httpResponse.getEntity());
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap myMap = objectMapper.readValue(dados, HashMap.class);

			String token = (String) myMap.get("access_token");

			String autenticacaoKCL = "Bearer " + token;
		//}
		return autenticacaoKCL;

	}
}
