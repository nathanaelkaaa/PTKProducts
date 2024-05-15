package com.example.springapi.api.controller;

import com.example.springapi.api.dto.ProductDTO;
import com.example.springapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable int id){
        Optional<ProductDTO> product = productService.getProduct(id);

        return product.map(productDTO -> new ResponseEntity<>(productDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<ProductDTO> products = productService.getProducts();

        if (!products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Void> postProduct(@RequestBody ProductDTO productDTO) {
        productService.postProduct(productDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Void> putProduct(@PathVariable int id, @RequestBody ProductDTO productDTO) {
        productService.putProduct(id, productDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
