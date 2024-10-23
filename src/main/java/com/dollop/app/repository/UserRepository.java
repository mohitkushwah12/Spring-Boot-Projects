package com.dollop.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dollop.app.entity.User;
import com.dollop.app.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findFirstByEmail(String email);

	User findByRole(UserRole userRole);
}
