package com.molla.repository;

import com.molla.model.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    public Long countById(Integer id);

    public Brand findByName(String name);
}
