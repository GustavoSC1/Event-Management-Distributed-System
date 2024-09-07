package com.gustavo.eventservice.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
	    info = @Info(
	        description = "API responsible for event management",
	        version = "V1.0.0",
	        title = "Event Service API",
	        contact = @Contact(
	            name = "Gustavo da Silva Cruz",
	            email = "gu.cruz17@hotmail.com",
	            url = "https://github.com/GustavoSC1"
	        ),
	        license = @License(
	            name = "Apache 2.0",
	            url = "http://www.apache.org/licenses/LICENSE-2.0"
	        )
	    ),
	    security = {@SecurityRequirement(name = "Keycloak")}
	)
@SecurityScheme(
	    name = "Keycloak"
	    , openIdConnectUrl = "http://localhost:8080/realms/eventmanagement/.well-known/openid-configuration"
	    , scheme = "bearer"
	    , type = SecuritySchemeType.OPENIDCONNECT
	    , in = SecuritySchemeIn.HEADER
	    )
public class OpenApiConfig {

}
