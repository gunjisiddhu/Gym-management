package com.epam.gymmanagementapplication.kafka;

import com.epam.gymmanagementapplication.dto.request.NotificationDTO;
import com.epam.gymmanagementapplication.dto.request.TrainingReportDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> sender;

    @Value("${topic.notify.channel}")
    private String notificationChannel;

    @Value("${topic.report.channel}")
    private String reportChannel;

    public void sendNotification(NotificationDTO notificationDTO) throws JsonProcessingException {
        String sendingString = new ObjectMapper().writeValueAsString(notificationDTO);
        sender.send(notificationChannel, sendingString);
    }


    public void sendTrainingReport(TrainingReportDTO reportDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String sendingString = objectMapper.writeValueAsString(reportDTO);
        sender.send(reportChannel, sendingString);
    }

}
