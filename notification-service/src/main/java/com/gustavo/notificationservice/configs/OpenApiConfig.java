package com.gustavo.notificationservice.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
	    info = @Info(
	        description = "API responsible for notification management",
	        version = "V1.0.0",
	        title = "Notification Service API",
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
	    security = {@SecurityRequirement(name = "bearerToken")}	
	)
@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {

}
