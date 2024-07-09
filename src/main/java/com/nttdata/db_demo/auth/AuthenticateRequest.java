package com.nttdata.db_demo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateRequest {

	private String name;
	private String password;
	
	public String getName() {
		return this.name;
	}
	
	public String getPassword() {
		return this.password;
	}
}
