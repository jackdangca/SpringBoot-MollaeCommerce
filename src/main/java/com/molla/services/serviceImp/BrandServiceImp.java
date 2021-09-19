package com.molla.services.serviceImp;

import com.molla.exciptions.BrandNotFoundException;
import com.molla.model.Brand;
import com.molla.model.Category;
import com.molla.repository.BrandRepository;
import com.molla.services.BrandService;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    @Override
    public String checkUnique(Integer id, String name) {
        return null;
    }

    @Override
    public void delete(Integer id) throws BrandNotFoundException {
        brandRepository.deleteById(id);
    }
}
