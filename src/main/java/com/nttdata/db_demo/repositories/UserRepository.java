package com.nttdata.db_demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.db_demo.entities.User;

/**
 * @author 319866
 * JPA Repository to perform database operation with Employee Data
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	Optional<User> findByName(String name);
}
