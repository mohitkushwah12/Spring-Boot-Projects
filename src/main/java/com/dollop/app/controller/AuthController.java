package com.dollop.app.controller;

import java.io.IOException;
import java.util.Optional;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dollop.app.dto.AuthenticationRequest;
import com.dollop.app.dto.SignupRequest;
import com.dollop.app.dto.UserDto;
import com.dollop.app.entity.User;
import com.dollop.app.repository.UserRepository;
import com.dollop.app.services.auth.AuthService;
import com.dollop.app.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController 
{
	private final AuthenticationManager authenticationManager;
	
	private final UserDetailsService userDetailsService;
	
	private final UserRepository userRepository;
	
	private final JwtUtil jwtUtil;
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String HEADER_STRING = "Authorization";
	
	private final AuthService authService;
	
	@PostMapping("/authenticate")
	public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException, JSONException
	{
		try 
		{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} 
		catch (BadCredentialsException e) 
		{
			throw new BadCredentialsException("Incurrect username or Password");
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		Optional<User> optional = userRepository.findFirstByEmail(userDetails.getUsername());
		final String jwt = jwtUtil.genrateToken(userDetails.getUsername());
		
		if(optional.isPresent())
		{
			response.getWriter().write(new JSONObject()
					.put("userId", optional.get().getId())
					.put("role", optional.get().getRole())
					.toString()
					);
			
			response.addHeader("Access-Control-Expose-Headers", "Authorization");
			response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, " + 
					"X-Requested-With, Content-Type, Accept, X-Custom-header");
			response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
		}
	}
	
	@PostMapping("/sign-up")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest)
	{
		if(authService.hasUserWithEmail(signupRequest.getEmail()))
		{
			return new ResponseEntity<>("User Already exists", HttpStatus.NOT_ACCEPTABLE);
		}
		
		UserDto userDto = authService.createUser(signupRequest);
		return new ResponseEntity<>(userDto, HttpStatus.CREATED);
	}
}


//final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.get());