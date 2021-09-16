package com.molla.controller;

import com.molla.services.CategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

	private final CategoryService service;

	public CategoryRestController(CategoryService service) {
		this.service = service;
	}

}
