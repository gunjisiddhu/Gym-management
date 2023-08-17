package com.epam.gymmanagementapplication.kafka;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopics {
    @Value("${topic.notify.channel}")
    private String notificationChannel;

    @Value("${topic.report.channel}")
    private String reportChannel;


    @Bean
    NewTopic createTopicForNotificationService(){
        return TopicBuilder.name(notificationChannel)
                .build();
    }

    @Bean
    NewTopic createTopicForReportService(){
        return TopicBuilder.name(reportChannel)
                .build();
    }

}
