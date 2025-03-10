package com.dollop.app.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.dollop.app.dto.SignupRequest;
import com.dollop.app.dto.UserDto;
import com.dollop.app.entity.Order;
import com.dollop.app.entity.User;
import com.dollop.app.enums.OrderStatus;
import com.dollop.app.enums.UserRole;
import com.dollop.app.repository.OrderRepository;
import com.dollop.app.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class AuthServiceImpl implements AuthService 
{
	@Autowired
	private UserRepository userRepository;
	
	@SuppressWarnings("unused")
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private OrderRepository orderRepository;
	
	public UserDto createUser(SignupRequest signupRequest)
	{
		User user = new User();
		user.setEmail(signupRequest.getEmail());
		user.setName(signupRequest.getName());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
		user.setRole(UserRole.CUSTOMER);
		User createdUser = userRepository.save(user);
		
		Order order = new Order();
		order.setAmount(0L);
		order.setTotalAmount(0L);
		order.setDiscount(0L);
		order.setUser(createdUser);
		order.setOrderStatus(OrderStatus.Pending);
		orderRepository.save(order);
		
		UserDto userDto = new UserDto();
		userDto.setId(createdUser.getId());
		
		return userDto;
	}
	
	public Boolean hasUserWithEmail(String email)
	{
		return userRepository.findFirstByEmail(email).isPresent();
	}
	
	@PostConstruct
	public void createAdminAccount()
	{
		User adminAccount = userRepository.findByRole(UserRole.ADMIN);
		if(null == adminAccount)
		{
			User user = new User();
			user.setEmail("admin123@gmail.com");
			user.setName("admin");
			user.setRole(UserRole.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}
	}
}
