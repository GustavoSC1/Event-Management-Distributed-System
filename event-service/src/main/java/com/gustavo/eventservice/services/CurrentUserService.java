package com.gustavo.eventservice.services;

import com.gustavo.eventservice.utils.UserAuthenticated;

public interface CurrentUserService {
	
	UserAuthenticated getCurrentUser();

}
