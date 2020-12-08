package br.com.alura.owasp.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Roles {
	
	
	@Id
	private String name;
	
	@Deprecated
	public Roles() {}
	
	public Roles(String role){
		this.name=role;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}