package com.copel.icl.seguranca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import com.copel.icl.externo.scs.FuncionalidadeDTO;
import com.copel.icl.externo.scs.PerfilDTO;
import com.copel.icl.externo.scs.SCSRest;

@Configuration
public class DecisorDeAcesso {

	@Autowired
	SCSRest scsRest;

	@Value("${app.sigla}")
	private String sigla;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DecisorDeAcesso.class);
	   
	
	public List<FuncionalidadeDTO> getFuncionalidadesUsuario(){
		String chaveUsuario = getChaveDeUsuarioDoToken();
		List<FuncionalidadeDTO> funcionalidades = new ArrayList<>();
		
		if( StringUtils.isNotBlank(chaveUsuario)) {
			List<PerfilDTO> perfis = scsRest.getPerfis(chaveUsuario, sigla);
		
			if( !CollectionUtils.isEmpty(perfis)) {
				funcionalidades.addAll(perfis.stream().map(PerfilDTO::getFuncionalidades).flatMap(List::stream).collect(Collectors.toList()));
			}
		}
		
		LOGGER.info("Funcionalidades autorizadas para chave {}: {}", chaveUsuario, funcionalidades.stream().map(FuncionalidadeDTO::getApelido).collect(Collectors.joining(", ")));
		
		return funcionalidades;
	}
	
	
	public List<PerfilDTO> getPerfisUsuario(){
		String chaveUsuario = getChaveDeUsuarioDoToken();
		List<PerfilDTO> perfis = new ArrayList<>();
		
		if( StringUtils.isNotBlank(chaveUsuario)) {
			perfis = scsRest.getPerfis(chaveUsuario, sigla);
		}
		
		LOGGER.info("Perfis para chave {}: {}", chaveUsuario, perfis.stream().map(PerfilDTO::getNome).collect(Collectors.joining(", ")));
		
		return perfis;
	}

	
	
	public boolean possuiPerfilSCS(Authentication auth, String nomePerfil) {
		String chaveDeUsuario = getChaveDeUsuarioDoToken(auth);
		boolean resposta = false;
		if (StringUtils.isNotBlank(chaveDeUsuario)) {
			resposta = scsRest.possuiPerfil(chaveDeUsuario, sigla, nomePerfil);
		}
		return resposta;
	}

	public boolean possuiFuncionalidadeSCS(String apelidoFuncionalidade) {
		return possuiFuncionalidadeSCS(SecurityContextHolder.getContext().getAuthentication(), apelidoFuncionalidade);
	}
	
	public boolean possuiFuncionalidadeSCS(Authentication auth, String apelidoFuncionalidade) {
		String chaveDeUsuario = getChaveDeUsuarioDoToken(auth);
		boolean resposta = false;
		if (StringUtils.isNotBlank(chaveDeUsuario)) {
			resposta = scsRest.possuiFuncionalidade(chaveDeUsuario, sigla, apelidoFuncionalidade);
		}
		return resposta;
	}

	public boolean possuiGrupo(Authentication auth, String grupo) {
		boolean resposta = false;

		AccessToken accessToken = getAccessToken(auth);
		if (accessToken != null) {
			ArrayList grupos = (ArrayList) accessToken.getOtherClaims().get("grupos");
			if (grupos == null) {
				grupos = new ArrayList();
			}
			resposta = grupos.contains(grupo) || grupos.contains("/" + grupo);

		}
		return resposta;
	}

	public boolean possuiGrupoBasic(Authentication auth, String grupo) {
		boolean resposta = false;
		if (auth == null || auth.getPrincipal() == null
				|| auth.getAuthorities() == null /* || !(auth.getPrincipal() instanceof ??????) */) {
			return resposta;
		}
		Collection<? extends GrantedAuthority> grupos = auth.getAuthorities();
		for (GrantedAuthority g : grupos) {
			if (("ROLE_" + grupo).equals(g.toString())) {
				resposta = true;
				break;
			}
		}
		return resposta;
	}

	private String getChaveDeUsuarioDoToken() {
		return getChaveDeUsuarioDoToken(SecurityContextHolder.getContext().getAuthentication());
	}
	
	private String getChaveDeUsuarioDoToken(Authentication auth) {
		String chave = null;

		AccessToken accessToken = getAccessToken(auth);
		if (accessToken != null) {

			// chave de usuario?
			String description = (String) accessToken.getOtherClaims().get("description");
			if (description != null) {
				chave = accessToken.getPreferredUsername();
			}
		}
		return chave;

	}

	private AccessToken getAccessToken(Authentication auth) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() == null || !(auth.getPrincipal() instanceof KeycloakPrincipal)) {
			return null;
		}
		KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) auth
				.getPrincipal();
		AccessToken accessToken = kp.getKeycloakSecurityContext().getToken();
		return accessToken;
	}
}
