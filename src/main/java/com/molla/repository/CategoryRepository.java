package com.molla.repository;

import com.molla.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public List<Category> findRootCategories(Sort sort);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
    public List<Category> search(String keyword);

    public Category findByName(String name);

    public Category findByAlias(String alias);

    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    public Long countById(Integer id);


}
