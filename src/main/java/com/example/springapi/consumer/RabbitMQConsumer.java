package com.example.springapi.consumer;

import com.example.springapi.api.dto.ProductDTO;
import com.example.springapi.publisher.RabbitMQProducer;
import com.example.springapi.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class RabbitMQConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private final ProductService productService;
    private final RabbitMQProducer producer;

    public RabbitMQConsumer(ProductService productService, RabbitMQProducer producer) {
        this.productService = productService;
        this.producer = producer;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public String consume(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        LOGGER.info(String.format("Received message -> %s", message));
        if(message.startsWith("ACTION")){
            String[] parts = message.split(":");
            switch (parts[0]){
                //Format : //ACTION_TYPE_ACTION:ID
                //Exemple : ACTION_GET_PRODUCT:1
                //TODO possibilité d'ajouter la queue de retour exemple  ACTION_GET_PRODUCT:1:orders

                case "ACTION_GET_PRODUCT":
                    LOGGER.info("Get product from RabbitMQ with id {}", parts[1]);
                    Optional<ProductDTO> productDTO = productService.getProduct(Integer.parseInt(parts[1]));
                    if(productDTO.isPresent()){
                        ProductDTO product = productDTO.get();
                        LOGGER.info("Return product to RabbitMQ {}", productDTO);
                        String json = objectMapper.writeValueAsString(product);
                        return json;
                    }
                    break;

                case "ACTION_BUY_PRODUCT":
                    if (parts.length == 3) {
                        LOGGER.info("Buy product from RabbitMQ with id {}", parts[1]);
                        Optional<ProductDTO> productDTO1 = productService.getProduct(Integer.parseInt(parts[1]));
                        ProductDTO product1 = productDTO1.get();

                        product1.setQuantity(product1.getQuantity() - Integer.parseInt(parts[2]));
                        productService.putProduct(Integer.parseInt(parts[1]),product1);
                    }
                    break;

                case "ACTION_ADD_PRODUCT":
                    if (parts.length == 3) {
                        LOGGER.info("Buy product from RabbitMQ with id {}", parts[1]);
                        Optional<ProductDTO> productDTO2 = productService.getProduct(Integer.parseInt(parts[1]));
                        ProductDTO product2 = productDTO2.get();
                        product2.setQuantity(product2.getQuantity() + Integer.parseInt(parts[2]));
                        productService.putProduct(Integer.parseInt(parts[1]), product2);
                    }
                    break;
            }
        }
        return null;
    }
}
