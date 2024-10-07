package com.pinup.global.kafka;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProduceService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.notifications.name}")
    private String notificationTopicName;

    public void sendNotification(String userEmail, String message) {
        kafkaTemplate.send(notificationTopicName, userEmail, message);
    }
}
