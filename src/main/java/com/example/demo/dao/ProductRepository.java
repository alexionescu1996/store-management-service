package com.example.demo.dao;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p from Product p WHERE p.deletedOn IS NULL")
    List<Product> findActiveProducts();

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deletedOn IS NULL")
    Optional<Product> findActiveProductById(@Param("id") Long id);

}

