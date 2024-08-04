package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/findById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findProductById(@PathVariable Long id) {
        logger.info("Find product by id: {}", id);
        ValidateUtil.validateId(id);

        var queryResult = productService.findActiveProductById(id);

        if (queryResult.isPresent()) {
            Product product = queryResult.get();
            logger.info("Product found: {}", product);
            return new ResponseEntity<>(queryResult.get(), HttpStatus.OK);
        }

        logger.warn("Product not found with id: {}", id);
        return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> save(@RequestBody List<Product> products) {
        logger.info("Save {} products.", products.size());

        List<Product> savedProducts = productService.saveAll(products);

        logger.info("Saved successfully: {} products.", savedProducts.size());

        return new ResponseEntity<>(savedProducts, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/updatePrice/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePrice(@PathVariable Long id, @RequestBody Double newPrice) {
        logger.info("Update price :{} for product with id: {}", newPrice, id);

        ValidateUtil.validateId(id);
        ValidateUtil.validatePrice(newPrice);

        Product updatedProduct = productService.updatePrice(id, newPrice);

        if (updatedProduct != null) {
            logger.info("Product updated with id: {} and new price: {}", updatedProduct.getId(), updatedProduct.getPrice());
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            logger.warn("Product not found or deleted with id: {}", id);
            return new ResponseEntity<>("Product not found or deleted.", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        logger.info("Delete product with id: {}", id);
        ValidateUtil.validateId(id);

        var queryResult = productService.findActiveProductById(id);
        if (queryResult.isPresent()) {
            Product product = queryResult.get();
            productService.delete(product);

            logger.info("Product: {} has been successfully deleted.", product);
            return new ResponseEntity<>("Product has been successfully deleted.", HttpStatus.OK);
        }

        logger.warn("Product with id: {} already deleted or not found", id);
        return new ResponseEntity<>("Product already deleted or not found.", HttpStatus.NOT_FOUND);
    }


}
