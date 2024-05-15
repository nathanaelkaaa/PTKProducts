package com.example.springapi.service;

import com.example.springapi.api.dto.ProductDTO;
import com.example.springapi.persistence.entity.ProductEntity;
import com.example.springapi.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<ProductDTO> getProduct(Integer id) {
        Optional<ProductEntity> productFromDB = productRepository.getProductEntityById(id);

        return productFromDB.map(this::productEntityMapper);
    }

    public List<ProductDTO> getProducts() {
        List<ProductEntity> productsFromDB = productRepository.findAll().stream().limit(10).collect(Collectors.toList());

        return productsFromDB.stream().map(this::productEntityMapper).collect(Collectors.toList());
    }

    public void postProduct(ProductDTO productDTO) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(generateId()); // Générer un nouvel identifiant
        productEntity.setLabel(productDTO.getLabel());
        productEntity.setPrice(productDTO.getPrice());
        productRepository.save(productEntity);
    }

    public void putProduct(Integer id, ProductDTO productDTO) {
        Optional<ProductEntity> optionalProductEntity = productRepository.getProductEntityById(id);
        optionalProductEntity.ifPresent(productEntity -> {
            productEntity.setLabel(productDTO.getLabel());
            productEntity.setPrice(productDTO.getPrice());
            productRepository.save(productEntity);
        });
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    private ProductDTO productEntityMapper(ProductEntity productEntity){
        return ProductDTO.builder()
                .label(productEntity.getLabel())
                .price(productEntity.getPrice())
                .build();
    }

    private int generateId() {
        // Implémentez la logique pour générer un identifiant unique, par exemple en utilisant un compteur ou une méthode de génération d'identifiant unique
        // Pour cet exemple, vous pouvez simplement utiliser un identifiant incrémental
        return Math.toIntExact(productRepository.count() + 1);
    }
}
