/**
 * 
 */
package com.nttdata.db_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nttdata.db_demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author 319866
 * Has beans
 */
@Configuration // so that spring app when starts it can inject this wherever its called on start
@RequiredArgsConstructor // if we declare / inject any final fields or beans here
public class ApplicationConfig {

	
	private final UserRepository userRepository;
	
	public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> userRepository.findByName(username).
				orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		//its a DAO to fetch user details , encode password etc
		//it has many implementations one of them is DaoAuthenticationProvider
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		//setting various imp details
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
		//Next step is to create an Authentication Manager which is used
		//Responsible for managing authentications
	}
	//need to see more on this
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
