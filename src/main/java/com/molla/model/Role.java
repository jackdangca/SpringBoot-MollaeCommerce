package com.molla.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 40, nullable = false, unique = true)
	private String name;
	
	@Column(length = 150, nullable = false)
	private String description;
	
	public Role(Integer id) {
		this.id = id;
	}

	public Role(String name) {
		this.name = name;
	}	
	
	public Role(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	

}
