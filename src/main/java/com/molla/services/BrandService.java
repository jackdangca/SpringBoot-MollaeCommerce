package com.molla.services;

import com.molla.model.Brand;

import java.util.List;

public interface BrandService {

    public List<Brand> listAll();

    public Brand save (Brand brand);
}
