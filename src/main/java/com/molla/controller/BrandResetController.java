package com.molla.controller;


import com.molla.services.BrandService;
import com.molla.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/brands")
public class BrandResetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrandResetController.class);

    private final BrandService brandService;

    public BrandResetController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
        LOGGER.info("BrandResetController | checkUnique");
        return brandService.checkUnique(id, name);
    }

}
