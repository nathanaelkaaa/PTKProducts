package com.example.springapi.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQProducer {

    //@Value("${rabbitmq.exchange.name}")
    @Value("${rabbitmq.exchange.orders.name}")
    private String exchange;

    //@Value("${rabbitmq.routing.key}")
    @Value("${rabbitmq.routing.orders.key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        LOGGER.info(String.format("Sending message -> %s", message));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);

    }
}
