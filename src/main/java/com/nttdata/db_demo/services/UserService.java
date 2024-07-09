package com.nttdata.db_demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.db_demo.entities.User;
import com.nttdata.db_demo.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repo;

	public List<User> getAllUsers() {
		return repo.findAll();
	}
	
	

}
