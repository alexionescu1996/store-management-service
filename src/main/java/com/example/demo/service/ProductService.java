package com.example.demo.service;


import com.example.demo.dao.ProductRepository;
import com.example.demo.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProduct() {
        return productRepository.findActiveProducts();
    }

    public Product save(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public Optional<Product> findActiveProductById(Long id) {
        return productRepository.findActiveProductById(id);
    }

    @Transactional
    public void delete(Product product) {
        product.setDeletedOn(LocalDateTime.now());
        productRepository.save(product);
    }

}