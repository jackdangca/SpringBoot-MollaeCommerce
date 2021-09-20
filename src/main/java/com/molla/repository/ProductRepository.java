package com.molla.repository;

import com.molla.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    public Product findByName(String name);

}
