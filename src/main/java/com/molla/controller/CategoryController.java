package com.molla.controller;

import com.molla.model.Category;
import com.molla.services.CategoryService;
import com.molla.util.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    @PostMapping("/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes ra) throws IOException {

        LOGGER.info("CategoryController | saveCategory is started");

        LOGGER.info("CategoryController | saveCategory | multipartFile.isEmpty() : " + multipartFile.isEmpty());

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            LOGGER.info("CategoryController | saveCategory | fileName : " + fileName);

            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);
            String uploadDir = "category-images/" + savedCategory.getId();

            LOGGER.info("CategoryController | saveCategory | savedCategory : " + savedCategory.toString());
            LOGGER.info("CategoryController | saveCategory | uploadDir : " + uploadDir);

            FileUpload.cleanDir(uploadDir);
            FileUpload.saveFile(uploadDir, fileName, multipartFile);
        } else {
            categoryService.save(category);
        }

        ra.addFlashAttribute("messageSuccess", "The category has been saved successfully.");
        return "redirect:/admin/categories";
    }



}
