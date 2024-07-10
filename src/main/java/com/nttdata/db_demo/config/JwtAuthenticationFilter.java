 package com.nttdata.db_demo.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component // to make this class a Bean
//We can make many no. of type of filter - here of type - OncePerRequestFilter
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JWTService jwtService;
	//UserDetailService is from Spring Security , we will implement this interface because
	//we need a method loadUserByUsername to fetch user from db using name
	//so we will create a class which enables it to be a bean for it
	private final UserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter(
	        JWTService jwtService,
	        UserDetailsService userDetailsService,
	        HandlerExceptionResolver handlerExceptionResolver
	    ) {
	        this.jwtService = jwtService;
	        this.userDetailsService = userDetailsService;
	        this.handlerExceptionResolver = handlerExceptionResolver;
	    }
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		//1. Check whether JWT Token exists or not
		
		//Since token is passed in Authorization header of our request we need that
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userName;
		//Now check for token existence
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			//If no token found then pass the request to next filter in the chain
			filterChain.doFilter(request, response);
			return;
		}
		try {
		//Extracting jwt token from auth header
		jwt = authHeader.substring(7);//7 because of Bearer string initially
		//Now we need to check whether the user already exists in the db
		//For that we need to extract username(can be anything like email,name which is unique in the records , here name)
		//from jwt token , so we create a JWTService class which has extractUsername(String token) method to do so
		userName = jwtService.extractUsername(jwt);// TODO:Extract user's name from jwt token	- Done
		
		//So, In JWTService jwt validation service has been made which has all the methods related to jwt
		//Now lets validate the jwt - username should not be null and check whether user is already authenticated or not 
		if(userName!=null && SecurityContextHolder.getContext().getAuthentication() ==null) {
			//Now since user not not null and is also not authenticated , so we will check user in db
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
			//We have fetched user details from the db using name now , check the token is valid or not
			//by validating token and user details (whether they are of same person or not)
			if(jwtService.isTokenValid(jwt, userDetails)) {
				// UsernamePasswordAuthenticationToken needed by Spring Security to update SecurityContextHolder
				//credentials is null because we don't have any currently
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				//to set more details for authToken
				authToken.setDetails(
						//enforcing request data into details of authToken
						//need to study about WebAuthenticationDetailsSource
						new WebAuthenticationDetailsSource().buildDetails(request));
				//Update the SecurityContextHolder
				SecurityContextHolder.getContext().setAuthentication(authToken);	
			}
			//Otherwise pass it to next filter chain method/action to be carried out
			// allow the HttpRequest to go to Spring's DispatcherServlet
	        // and @RestControllers/@Controllers.
			filterChain.doFilter(request, response);
			//Now next step is to bind all these things - the JwtAuthenticationFilter we created,jwt service and SpringContextHolders 
			//For that we need to define the configuration we are going to use and define a Configuration Class like SecurityConfig class here
		}}
		catch(Exception exception) {
			handlerExceptionResolver.resolveException(request, response, null, exception);
		}
	}

}
