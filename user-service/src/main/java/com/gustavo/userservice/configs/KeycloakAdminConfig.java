package com.gustavo.userservice.configs;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {
	
	@Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.username}")
    private String adminUsername;

    @Value("${keycloak.password}")
    private String adminPassword;
    
    @Bean
    public Keycloak keycloakAdmin() {
    	return KeycloakBuilder.builder()
        		
                .serverUrl("http://localhost:8080")
                .realm("eventmanagement")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("gateway")
                .clientSecret("1vAYC2svFaNfa3Xht9q12S7azdqhksvP")
                .build();
        		
        /*
              
        		.serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(adminUsername)
                .password(adminPassword)
                .build();
                 */
        
        /*
        view-users
		manage-users
		view-realm
		realm-admin
		query-users
		view-clients
        */
    }

}
