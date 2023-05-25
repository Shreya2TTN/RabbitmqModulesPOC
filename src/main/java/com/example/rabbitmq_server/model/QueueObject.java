package com.example.rabbitmq_server.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueueObject {

    private String type;

    private LocalDateTime time;

}
