package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Product> getAllProduct() {

        return productService.getAllProduct();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    @PostMapping("/saveAll")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Product> saveAll(@RequestBody List<Product> products) {
        return productService.saveAll(products);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Product update(@RequestBody Product product) {
        return productService.save(product);
    }


}
