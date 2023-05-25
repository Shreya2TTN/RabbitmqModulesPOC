package com.example.rabbitmq_server.controller;

import com.example.rabbitmq_server.model.QueueObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
public class HeaderController {
    @Autowired
    AmqpTemplate headerExchange;

    @PostMapping("/header")
    public ResponseEntity<?> sendMessageWithHeaderExchange(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "debug", required = false) String debug,
            @RequestParam(value = "info", required = false) String info,
            @RequestParam(value = "warning", required = false) String warning) {
        QueueObject object=new QueueObject("header", LocalDateTime.now());
        MessageBuilder builder=MessageBuilder.withBody(object.toString().getBytes());
        if(Objects.nonNull(error)){
            builder.setHeader("error",error);
        }
        if(Objects.nonNull(debug)){
            builder.setHeader("debug",debug);
        }
        if(Objects.nonNull(info)){
            builder.setHeader("info",info);
        }
        if(Objects.nonNull(warning)){
            builder.setHeader("warning",warning);
        }
        headerExchange.convertAndSend(builder.build());
        return ResponseEntity.ok(true);
    }
}
