package com.gustavo.notificationservice.controllers.exceptions;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
	
	Logger log = LogManager.getLogger(JWTAccessDeniedHandler.class);
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
	        AccessDeniedException accessDeniedException) throws IOException {
		log.error("Forbidden error: {}", accessDeniedException.getMessage());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.getWriter().write("Error: User does not have permission to access this resource!");
	}

}
