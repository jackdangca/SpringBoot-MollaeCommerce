package com.molla.services.serviceImp;

import com.molla.exciptions.BrandNotFoundException;
import com.molla.model.Brand;
import com.molla.model.Category;
import com.molla.repository.BrandRepository;
import com.molla.services.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transient
public class BrandServiceImp implements BrandService {

    public static final int BRANDS_PER_PAGE = 4;

    private final BrandRepository brandRepository;

    public BrandServiceImp(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> listAll() {
        return (List<Brand>) brandRepository.findAll();
    }

    @Override
    public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);

        if (keyword != null) return brandRepository.findAll(keyword, pageable);
        else return brandRepository.findAll(pageable);

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
        boolean isNew = (id == null || id == 0);
        Brand brand = brandRepository.findByName(name);

        if (isNew) {
            if (brand != null) return "Duplicate";
        } else {
            if (brand != null && brand.getId() != id) {
                return "Duplicate";
            }
        }

        return "OK";

    }

    @Override
    public void delete(Integer id) throws BrandNotFoundException {
        brandRepository.deleteById(id);
    }
}
