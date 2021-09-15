package com.molla.controller;

import com.molla.services.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
public class UserRestController {
	
	private final UserService service;

	public UserRestController(UserService service) {
		this.service = service;
	}

	@PostMapping("/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id,
									  @RequestParam("email") String email) {
		return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
}
