package com.example.rabbitmq_server.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderExchangeConfig {
    @Autowired
    AmqpAdmin amqpAdmin;
    @Value("${rabbitmq.header.queue-1}")
    private String HEADER_QUEUE_1;
    @Value("${rabbitmq.header.queue-2}")
    private String HEADER_QUEUE_2;

    @Value("${rabbitmq.header.exchange}")
    private String HEADER_EXCHANGE;
    Queue createHeaderQueue1(){
        return new Queue(HEADER_QUEUE_1,true,false,false);
    }
    Queue createHeaderQueue2(){
        return new Queue(HEADER_QUEUE_2,true,false,false);
    }
    HeadersExchange createExchange(){
        return new HeadersExchange(HEADER_EXCHANGE,true,false);
    }
    Binding createBinding1(){
        return BindingBuilder.bind(createHeaderQueue1()).to(createExchange())
                .whereAll("error","debug").exist();
    }
    Binding createBinding2(){
        return BindingBuilder.bind(createHeaderQueue2()).to(createExchange())
                .whereAny("info","warning").exist();
    }
    @Bean
    public AmqpTemplate headerExchange(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(HEADER_EXCHANGE);
        return rabbitTemplate;
    }
    @PostConstruct
    public void init(){
        amqpAdmin.declareQueue(createHeaderQueue1());
        amqpAdmin.declareQueue(createHeaderQueue2());
        amqpAdmin.declareExchange(createExchange());
        amqpAdmin.declareBinding(createBinding1());
        amqpAdmin.declareBinding(createBinding2());
    }
}
