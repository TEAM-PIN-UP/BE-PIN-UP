package com.pinup.global.kafka;

import com.pinup.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final NotificationService notificationService;

    @KafkaListener(topics = "pinup.user.notifications", groupId = "pinup-user-notification-processor", concurrency = "3")
    void notificationListener(ConsumerRecord<String, String> record) {
        String userEmail = record.key();
        String message = record.value();
        notificationService.sendNotification(userEmail, message);
    }
}