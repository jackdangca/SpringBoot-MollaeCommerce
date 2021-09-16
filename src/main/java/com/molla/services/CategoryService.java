package com.molla.services;

import com.molla.exciptions.CategoryNotFoundException;
import com.molla.model.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> listAll();

    public List<Category> listCategoriesUsedInForm();

    public Category save(Category category);

    public Category get(Integer id) throws CategoryNotFoundException;
}
