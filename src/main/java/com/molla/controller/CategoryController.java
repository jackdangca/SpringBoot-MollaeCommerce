package com.molla.controller;

import com.molla.model.Category;
import com.molla.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String listFirstPage(String sortDir, Model model) {

        model.addAttribute("categories", categoryService.listAll());

        return "categories/index";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {

        LOGGER.info("CategoryController | newCategory is started");

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("categories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");

        LOGGER.info("CategoryController | newCategory | listCategories : " + listCategories.toString());

        return "categories/category_form";

    }




}
