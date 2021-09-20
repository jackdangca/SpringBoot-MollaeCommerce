package com.molla.services.serviceImp;

import com.molla.model.Product;
import com.molla.repository.ProductRepository;
import com.molla.services.ProductService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImp(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }
}
