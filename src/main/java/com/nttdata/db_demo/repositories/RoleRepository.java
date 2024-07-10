package com.nttdata.db_demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.db_demo.constants.RoleEnum;
import com.nttdata.db_demo.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
	Optional<Role> findByName(RoleEnum name);
}
