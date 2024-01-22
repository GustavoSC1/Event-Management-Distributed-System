package com.gustavo.notificationservice.configs;

import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.gustavo.notificationservice.controllers.exceptions.JWTAccessDeniedHandler;
import com.gustavo.notificationservice.controllers.exceptions.JWTAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Value("${jwt.public.key}")
	private RSAPublicKey key;
	
	@Autowired
	private JWTAccessDeniedHandler jwtAccessDeniedHandler;
	
	@Autowired
	private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(authorize -> authorize
				.anyRequest().authenticated())
		.oauth2ResourceServer(
	            conf -> conf
	            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	            .accessDeniedHandler(jwtAccessDeniedHandler)
	            .jwt(
	                jwt -> jwt.decoder(jwtDecoder())
	                .jwtAuthenticationConverter(jwtAuthenticationConverter())));
		
		http.headers(headers -> headers
			.frameOptions(frameOptions -> frameOptions
		    	.sameOrigin()));
		
		return http.build();
	}
	
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}
		
	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.key).build();
	}

}
