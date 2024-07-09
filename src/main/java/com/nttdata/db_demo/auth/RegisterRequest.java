package com.nttdata.db_demo.auth;
import com.nttdata.db_demo.constants.Role;

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
	
	public String getName() {
		return this.name;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getDesignation() {
		return this.designation ;
	}
	
	
}
