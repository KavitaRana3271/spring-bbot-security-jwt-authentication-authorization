package com.nttdata.db_demo.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nttdata.db_demo.constants.Role;
import com.nttdata.db_demo.entities.User;
import com.nttdata.db_demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

	public User register(RegisterRequest request) {
		User user = new User();
		user.setName(request.getName());
		user.setDesignation(request.getDesignation());
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
