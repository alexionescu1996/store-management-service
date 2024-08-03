package com.example.demo.controller;

import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAll")
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findProductById(@PathVariable Long id) {
        var queryResult = productService.findById(id);
        if (queryResult.isPresent()) {
            return new ResponseEntity<>(queryResult.get(), HttpStatus.OK);
        } else {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }

    @PostMapping("/save")
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
