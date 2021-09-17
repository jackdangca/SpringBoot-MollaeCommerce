package com.molla.services;

import com.molla.exciptions.CategoryNotFoundException;
import com.molla.model.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> listAll(String sortDir, String keyword);

    public List<Category> listCategoriesUsedInForm();

    public Category save(Category category);

    public Category get(Integer id) throws CategoryNotFoundException;

    public String checkUnique(Integer id, String name, String alias);

    public void updateCategoryEnabledStatus(Integer id, boolean enabled);

    public void delete(Integer id) throws CategoryNotFoundException;

}
