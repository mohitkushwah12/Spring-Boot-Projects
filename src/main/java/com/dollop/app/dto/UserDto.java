package com.dollop.app.dto;

import com.dollop.app.enums.UserRole;

import lombok.Data;

@Data
public class UserDto 
{
	private Long id;
	private String name;
	private String email;
	private String password;
	private UserRole userRole;
}
