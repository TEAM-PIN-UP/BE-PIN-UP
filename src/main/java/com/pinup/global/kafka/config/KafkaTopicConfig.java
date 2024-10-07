package com.pinup.global.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.notifications.name}")
    private String notificationTopicName;

    @Value("${kafka.topic.notifications.partitions}")
    private int notificationTopicPartitions;

    @Value("${kafka.topic.notifications.replicas}")
    private short notificationTopicReplicas;

    @Bean
    public NewTopic userNotificationsTopic() {
        return TopicBuilder.name(notificationTopicName)
                .replicas(notificationTopicReplicas)
                .partitions(notificationTopicPartitions)
                .build();
    }
}
