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
import org.springframework.web.bind.annotation.PostMapping;

@Configuration
public class TopicExchangeConfig {
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Value("${rabbitmq.topic.queue-1}")
    private String TOPIC_QUEUE_1;
    @Value("${rabbitmq.topic.queue-2}")
    private String TOPIC_QUEUE_2;
    @Value("${rabbitmq.topic.queue-3}")
    private String TOPIC_QUEUE_3;
    @Value("${rabbitmq.topic.exchange}")
    private String TOPIC_EXCHANGE;
    @Value("${rabbitmq.topic.pattern-1}")
    private String TOPIC_PATTERN_1;
    @Value("${rabbitmq.topic.pattern-2}")
    private String TOPIC_PATTERN_2;
    @Value("${rabbitmq.topic.pattern-3}")
    private String TOPIC_PATTERN_3;

    Queue createTopicQueue1() {
        return new Queue(TOPIC_QUEUE_1, true, false, false);
    }

    Queue createTopicQueue2() {
        return new Queue(TOPIC_QUEUE_2, true, false, false);
    }

    Queue createTopicQueue3() {
        return new Queue(TOPIC_QUEUE_3, true, false, false);
    }

    TopicExchange createTopicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    Binding createTopicBinding1() {
        return BindingBuilder.bind(createTopicQueue1()).to(createTopicExchange()).with(TOPIC_PATTERN_1);
    }

    Binding createTopicBinding2() {
        return BindingBuilder.bind(createTopicQueue2()).to(createTopicExchange()).with(TOPIC_PATTERN_2);
    }

    Binding createTopicBinding3() {
        return BindingBuilder.bind(createTopicQueue3()).to(createTopicExchange()).with(TOPIC_PATTERN_3);
    }

    @Bean
    public AmqpTemplate topicExchange(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(TOPIC_EXCHANGE);
        return rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(createTopicQueue1());
        amqpAdmin.declareQueue(createTopicQueue2());
        amqpAdmin.declareQueue(createTopicQueue3());
        amqpAdmin.declareExchange(createTopicExchange());
        amqpAdmin.declareBinding(createTopicBinding1());
        amqpAdmin.declareBinding(createTopicBinding2());
        amqpAdmin.declareBinding(createTopicBinding3());
    }
}
