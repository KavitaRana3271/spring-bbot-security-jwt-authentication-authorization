package com.nttdata.db_demo.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.db_demo.config.JWTService;
import com.nttdata.db_demo.entities.User;

import lombok.RequiredArgsConstructor;

/**
 * @author 319866
 * This class holds urls where authentication is required
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final JWTService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JWTService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody RegisterRequest request){
		 	User registeredUser = authenticationService.register(request);
	        return ResponseEntity.ok(registeredUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticateRequest request){
		 User authenticatedUser = authenticationService.authenticate(request);

	        String jwtToken = jwtService.generateToken(authenticatedUser);

	        AuthenticationResponse authResponse = new AuthenticationResponse();
	        authResponse.setToken(jwtToken);

	        return ResponseEntity.ok(authResponse);
	}
}
