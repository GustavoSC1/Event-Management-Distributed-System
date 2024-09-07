package com.gustavo.apigateway.configs;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class OpenApiConfig {
		
	@Bean
	// Tem que ser carregado assim que API Gateway estiver inicializando
	@Lazy(false)
	public List<GroupedOpenApi> apis(
			SwaggerUiConfigParameters config, 
			RouteDefinitionLocator locator) {
		
		List<GroupedOpenApi> groups = new ArrayList<>();
		
		// Obtem todas as rotas definidas para serviços. 
		// As páginas de documentação viram objetos definition
		// .block() porque enquanto estiver lendo é necessário bloquear o arquivo
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		
		definitions.stream().filter(
					routeDefinition -> routeDefinition.getId()
						.matches(".*-service")) // Obtem as rotas que que possuem service no nome (Ex: book-service e cambio-service)
							.forEach(routeDefinition -> {
								String name = routeDefinition.getId();
								// Busca o id de cada rota e o define como um nome de grupo.
								config.addGroup(name);								
								// Permite dividir as definições de OpenAPI em diferentes grupos com um determinado nome.
								// Como resultado, temos vários recursos OpenAPI no caminho /v3/api-docs/{SERVICE_NAME}, 
								// por exemplo, /v3/api-docs/cambio-service.
								groups.add(GroupedOpenApi.builder() // Constrói o conjunto de documentações Swagger
									.pathsToMatch("/" + name + "/**")
									.group(name).build());
							});
		
		return groups;
	}

}