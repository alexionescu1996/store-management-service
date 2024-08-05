package com.example.demo.controller;


import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetALlProductsWhenSuccess() throws Exception {
        Product product1 = new Product(1L, "testProduct1", 12.4);
        Product product2 = new Product(2L, "testProduct12", 136.2);

        when(productService.getAllProduct()).thenReturn(Arrays.asList(product1, product2));

        MvcResult result = mockMvc.perform(get("/products/getAll"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Product[] products = objectMapper.readValue(jsonResponse, Product[].class);

        assertEquals(2, products.length);

        verify(productService, times(1)).getAllProduct();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllProductsWhenEmptyList() throws Exception {
        when(productService.getAllProduct()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products/getAll"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andExpect(content().string("Product list is empty."));

        verify(productService, times(1)).getAllProduct();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testFindProductByIdWhenSuccess() throws Exception {
        Product product = new Product(1L, "Product1", 123.4);
        when(productService.findActiveProductById(anyLong())).thenReturn(Optional.of(product));

        MvcResult result = mockMvc.perform(get("/products/findById/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Product returnedProduct = objectMapper.readValue(json, Product.class);

        assertEquals("Product1", returnedProduct.getName());
        assertEquals(123.4, returnedProduct.getPrice());

        verify(productService, times(1)).findActiveProductById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testFindProductByIdWhenNotFound() throws Exception {
        when(productService.findActiveProductById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/findById/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found."));

        verify(productService, times(1)).findActiveProductById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSaveProductsWhenSuccess() throws Exception {
        Product product1 = new Product(1L, "Test444", 1.0);
        Product product2 = new Product(2L, "tes445", 2.0);
        when(productService.saveAll(any())).thenReturn(Arrays.asList(product1, product2));

        MvcResult result = mockMvc.perform(post("/products/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productsJson))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Product[] returnedProducts = objectMapper.readValue(json, Product[].class);

        assertEquals(2, returnedProducts.length);
        assertEquals("Test444", returnedProducts[0].getName());
        assertEquals(1, returnedProducts[0].getPrice());

        verify(productService, times(1)).saveAll(any());
    }

    String productsJson = "[\n" +
            "    {\n" +
            "        \"name\": \"Test444\",\n" +
            "        \"price\": 1\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"tes445\",\n" +
            "        \"price\": 2\n" +
            "    }\n" +
            "]";

}
