package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProduct() {
        logger.info("Start get all products.");

        List<Product> resultList = productService.getAllProduct();
        if (resultList.isEmpty()) {
            logger.info("Product list is empty.");
            return new ResponseEntity<>("Product list is empty.", HttpStatus.NO_CONTENT);
        }

        logger.info("Products list size: {}", resultList.size());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findProductById(@PathVariable Long id) {
        logger.info("Find product by id: {}", id);

        var queryResult = productService.findActiveProductById(id);

        if (queryResult.isPresent()) {
            Product product = queryResult.get();
            logger.info("Product found: {}", product);
            return new ResponseEntity<>(queryResult.get(), HttpStatus.OK);
        }

        logger.warn("Product not found with id: {}", id);
        return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Product>> save(@RequestBody List<Product> products) {
        logger.info("Save {} products.", products.size());

        List<Product> savedProducts = productService.saveAll(products);

        logger.info("Saved successfully {} products.", savedProducts.size());

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
        var queryResult = productService.findActiveProductById(id);
        if (queryResult.isPresent()) {
            Product product = queryResult.get();
            productService.delete(product);

            logger.info("Product :{} has been successfully deleted.", product);
            return new ResponseEntity<>("Product has been successfully deleted.", HttpStatus.OK);
        }

        logger.warn("Product with id {} already deleted or not found", id);
        return new ResponseEntity<>("Product already deleted or not found.", HttpStatus.NOT_FOUND);
    }


}
