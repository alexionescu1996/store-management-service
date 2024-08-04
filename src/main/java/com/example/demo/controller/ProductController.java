package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllProduct() {
        List<Product> resultList = productService.getAllProduct();

        if (resultList.isEmpty())
            return new ResponseEntity<>("Product list is empty.", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findProductById(@PathVariable Long id) {
        var queryResult = productService.findById(id);
        if (queryResult.isPresent()) {
            return new ResponseEntity<>(queryResult.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Product>> saveAll(@RequestBody List<Product> products) {
        List<Product> savedProducts = productService.saveAll(products);
        return new ResponseEntity<>(savedProducts, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(@RequestBody Product product) {
        Product updatedProduct = productService.save(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.delete(id);
        return new ResponseEntity<>("Product has been successfully deleted.", HttpStatus.OK);
    }


}
