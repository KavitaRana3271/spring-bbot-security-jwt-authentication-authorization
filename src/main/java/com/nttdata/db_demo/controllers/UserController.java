package com.nttdata.db_demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.db_demo.entities.User;
import com.nttdata.db_demo.repositories.UserRepository;
import com.nttdata.db_demo.services.UserService;

@RequestMapping("/api/v1/demo/users")
@RestController
public class UserController {
	
@Autowired
private UserService service;

@GetMapping("/me")
public ResponseEntity<User> getAuthenticatedUser(){
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	User currentUser = (User) authentication.getPrincipal();
	return ResponseEntity.ok(currentUser);
}

@GetMapping("/")
public ResponseEntity<List<User>> getAllUsers(){
	return ResponseEntity.ok(service.getAllUsers());
}
}

