/**
 * 
 */
package com.nttdata.db_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * @author 319866
 * This is the config class for binding security related services and filters
 * It has a bean of type SecurityFilterChain that is looked upon by Spring Security
 * on the application start up
 */
@Configuration // to make it a config class
@EnableWebSecurity // since security is involved here
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	
	public SecurityConfiguration(
	        JwtAuthenticationFilter jwtAuthFilter,
	        AuthenticationProvider authenticationProvider
	    ) {
			this.jwtAuthFilter = jwtAuthFilter;
	        this.authenticationProvider = authenticationProvider;
	    }
	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf() // need to disable csrf , reason has to be found out
		.disable()
		.authorizeHttpRequests() // no need to authenticate below matching urls
		.requestMatchers("/api/v1/auth/**")
		.permitAll()
		.anyRequest() // apart from above whitelisted urls should be authenticated
		.authenticated()
		//now session mgmt,since we implemented OncePerRequest type of authentication filter ,
		//so no authentication should be saved instead every new request should call a 
		//new authentication hence it will be stateless
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and() 
		//Adding AuthenticationProvider object that will be created separately
		//Create a bean in ApplicationConfig file
		.authenticationProvider(authenticationProvider)
		//use the jwt filter we just created , using addFilterBefore because
		//Need to run the filter created by us - JWTAuthenticationFilter object before UsernamePasswordAuthenticationFilter class
		.addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class);	
		
		return http.build();
	}
}
