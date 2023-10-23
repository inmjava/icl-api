package com.copel.icl.seguranca;

import java.util.Arrays;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ConfigSeguranca {
	
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

	
	

	@Order(1)
	@Configuration
	//@Profile("isolada")
	public static class LdapConfig extends WebSecurityConfigurerAdapter {

		@Value("${ldap.bind-user-dn}")
		private String bindUserDn;

		@Value("${ldap.bind-user-pwd}")
		private String bindUserPwd;
		
		@Value("${ldap.user-search-base}")
		private String userSearchBase; 
		
		@Value("${ldap.user-search-filter}")
		private String userSearchFilter;
		
		@Value("${ldap.group-search-base}")
		private String groupSearchBase;
		
		@Value("${ldap.group-search-filter}")
		private String groupSearchFilter;
		
		@Value("${ldap.url-x}")
		private String urlX;
		

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
			authBuilder.ldapAuthentication().userSearchBase(userSearchBase).userSearchFilter(userSearchFilter)

					.groupSearchBase(groupSearchBase).groupSearchFilter(groupSearchFilter)

					.contextSource().url(urlX)

					.managerDn(bindUserDn).managerPassword(bindUserPwd);
					
		}

		
		@Override
		public void configure(HttpSecurity http) throws Exception {
			
			http.cors().and()
			.antMatcher("/demo/seg/basic/**").authorizeRequests()	
				.antMatchers("/demo/seg/basic/user").authenticated()
				.antMatchers("/demo/seg/basic/publico").permitAll()
				.antMatchers("/demo/seg/basic/**").authenticated()

			.antMatchers("/actuator/**").denyAll()
			.and().httpBasic().and().csrf()
			.disable();
			
		
		}


		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/monitor**", "/swagger**", "/v3/api-docs/**", "/actuator/health/liveness", "/actuator/health/readiness");			
		}

	}

	
	@Order(2)
	@Configuration
	@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
	public static class KeyCloakConfig extends KeycloakWebSecurityConfigurerAdapter {

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
			KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		
			
			keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
			authBuilder.authenticationProvider(keycloakAuthenticationProvider);
		}


		
		@Bean
		public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
			return new KeycloakSpringBootConfigResolver();
		}
		

		@Override
		protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
			return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
		
			super.configure(http);
			http.exceptionHandling()
		    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and().
			
			sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors()
			.and()
			.authorizeRequests()
			.antMatchers("/demo/seg/bearer/publico").permitAll()
			.antMatchers("/demo/seg/bearer/**").authenticated()
			.antMatchers("/demo/externo/rhj/**").permitAll()
			.antMatchers("/profissional/**").permitAll()
			.antMatchers("/proxy/**").authenticated()
			.antMatchers("/tarefas/**").permitAll()
			.antMatchers("/**").denyAll()
			.and().csrf().disable();
		}

		  @Bean
		  CorsConfigurationSource corsConfigurationSource() {
		      CorsConfiguration configuration = new CorsConfiguration();
		      configuration.setAllowedOrigins(Arrays.asList("*"));
		      configuration.setAllowedMethods(Arrays.asList("*"));
		      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		      source.registerCorsConfiguration("/**", configuration);
		      return source;
		  }

		  /*
        @Bean
        public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(KeycloakAuthenticationProcessingFilter filter) {
            FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
            registrationBean.setEnabled(false);
            return registrationBean;
        }

       @Bean
        public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(KeycloakPreAuthActionsFilter filter) {
            FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
            registrationBean.setEnabled(false);
            return registrationBean;
        }
        */

	}
	

}
