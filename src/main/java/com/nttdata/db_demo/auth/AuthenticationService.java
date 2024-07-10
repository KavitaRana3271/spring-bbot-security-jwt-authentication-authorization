package com.nttdata.db_demo.auth;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nttdata.db_demo.constants.RoleEnum;
import com.nttdata.db_demo.entities.Role;
import com.nttdata.db_demo.entities.User;
import com.nttdata.db_demo.repositories.RoleRepository;
import com.nttdata.db_demo.repositories.UserRepository;


@Service
public class AuthenticationService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

	public User register(RegisterRequest request) {
		Optional<Role> optionalRole = roleRepository.findByName(request.getRole());
		
		if(optionalRole.isEmpty()) {
			return null;
		}
		
		User user = new User();
		user.setName(request.getName());
		user.setDesignation(request.getDesignation());
		user.setRole(optionalRole.get());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		return userRepository.save(user);

	}

	public User authenticate(AuthenticateRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getName(),
                request.getPassword()
        ));
		
		return userRepository.findByName(request.getName()).orElseThrow();
	}

}
