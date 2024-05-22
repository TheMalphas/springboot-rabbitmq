package net.javaguides.springboot.controller;

import net.javaguides.springboot.publisher.RabbitMQProducer;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private RabbitMQProducer producer;

    public MessageController(RabbitMQProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message){
    try {
            producer.sendMessage(message);
            return ResponseEntity.ok("Message sent to RabbitMQ ...");
        } catch (AmqpConnectException e) {
            return ResponseEntity.status(500).body("Failed to send message to RabbitMQ: " + e.getMessage());
        }
    }

    @ExceptionHandler(AmqpConnectException.class)
    public ResponseEntity<String> handleAmqpConnectException(AmqpConnectException e) {
        return ResponseEntity.status(500).body("RabbitMQ connection error: " + e.getMessage());
    }

}
