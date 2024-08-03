package com.example.demo.aspect;

import com.example.demo.exception.InvalidProductException;
import com.example.demo.model.Product;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class ValidationAspect {

    private static final Logger logger = LoggerFactory.getLogger(ValidationAspect.class);

    @Before("execution(* com.example.demo.service.ProductService.save(..)) && args(product)")
    public void validateSingleProduct(Product product) {
        validateProductDetails(product);
    }

    @Before("execution(* com.example.demo.service.ProductService.saveAll(..)) && args(products)")
    public void validateMultipleProducts(List<Product> products) {
        for (Product product : products) {
            validateProductDetails(product);
        }
    }

    private void validateProductDetails(Product product) {
        logger.info("Validating product: {}", product);
        if (product.getName() == null || product.getName().isEmpty()) {
            logger.error("Validation failed for product: {}", product);
            throw new InvalidProductException("Product name cannot be null or empty");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            logger.error("Validation failed for product: {}", product);
            throw new InvalidProductException("Product price cannot be null or negative");
        }
        logger.info("Validation successful for product: {}", product);
    }
}
