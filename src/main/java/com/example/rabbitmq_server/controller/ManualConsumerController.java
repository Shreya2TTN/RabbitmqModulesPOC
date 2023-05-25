package com.example.rabbitmq_server.controller;

import com.example.rabbitmq_server.consumer.ManualConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManualConsumerController {
    @Autowired
    ManualConsumer manualConsumer;

    @PostMapping("/consume/{queueName}")
    public ResponseEntity<?> consumeAllMessagesOfQueue(@PathVariable String queueName){
        return ResponseEntity.ok(manualConsumer.receiveMessages(queueName));
    }
}
