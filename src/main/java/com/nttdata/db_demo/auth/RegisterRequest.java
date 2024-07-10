package com.nttdata.db_demo.auth;
import com.nttdata.db_demo.constants.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	private String name;
	private String password;
	private String designation;
	private RoleEnum role;	
}
