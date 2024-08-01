package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAll")
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }

    @PostMapping("/save")
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    @PostMapping("/saveAll")
    public List<Product> saveAll(@RequestBody List<Product> products) {
        return productService.saveAll(products);
    }

    @PutMapping("/update")
    public Product update(@RequestBody Product product) {
        return productService.save(product);
    }


}
