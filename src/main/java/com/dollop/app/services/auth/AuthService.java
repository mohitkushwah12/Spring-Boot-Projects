package com.dollop.app.services.auth;

import com.dollop.app.dto.SignupRequest;
import com.dollop.app.dto.UserDto;

public interface AuthService 
{
	UserDto createUser(SignupRequest signupRequest);
	
	Boolean hasUserWithEmail(String email);
}
