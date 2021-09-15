package com.molla.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 128, nullable = false, unique = true)
	private String email;

	@Column(length = 64, nullable = false)
	private String password;

	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;

	private String photos;
	private boolean enabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();

	public User(String email) {
		this.email = email;
	}

	public User(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}


	@Transient
	public String getPhotoImagePath() {
		if (id == null || photos == null) return "/images/profile-default.png";
		return "/user-photos/" + this.id + "/" + this.photos;
	}

	public void addRole(Role role) {
		this.roles.add(role);
	}

	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();

		while (iterator.hasNext()) {
			Role role = iterator.next();
			if (role.getName().equals(roleName)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", name='" + getFullName() + '\'' +
				", photos='" + photos + '\'' +
				", enabled=" + enabled +
				", roles=" + roles +
				'}';
	}
}
