package com.gustavo.userservice.services;

import com.gustavo.userservice.utils.UserAuthenticated;

public interface CurrentUserService {
	
	UserAuthenticated getCurrentUser();

}
