package com.molla.services;

import com.molla.exciptions.BrandNotFoundException;
import com.molla.exciptions.CategoryNotFoundException;
import com.molla.model.Brand;
import com.molla.model.Category;

import java.util.List;

public interface BrandService {

    public List<Brand> listAll();

    public Brand save (Brand brand);

    public Brand get(Integer id) throws BrandNotFoundException;

    public String checkUnique(Integer id, String name);

    public void delete(Integer id) throws BrandNotFoundException;

}
