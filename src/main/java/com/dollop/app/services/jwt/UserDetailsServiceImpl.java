package com.dollop.app.services.jwt;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.dollop.app.entity.User;
import com.dollop.app.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> optional = userRepository.findFirstByEmail(username);
		if(optional.isEmpty()) throw new UsernameNotFoundException("Username Not Found", null);
		return new org.springframework.security.core.userdetails.User(optional.get().getEmail(), optional.get().getPassword()
				,new ArrayList<>());
				
	}
	
}

//.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));