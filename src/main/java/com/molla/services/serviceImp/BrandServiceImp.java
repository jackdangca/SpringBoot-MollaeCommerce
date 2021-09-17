package com.molla.services.serviceImp;

import com.molla.model.Brand;
import com.molla.repository.BrandRepository;
import com.molla.services.BrandService;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transient
public class BrandServiceImp implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImp(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> listAll() {
        return (List<Brand>) brandRepository.findAll();
    }
}
