package com.epam.gymreportmanagement.kafka;

import com.epam.gymreportmanagement.dto.TrainingReportDTO;
import com.epam.gymreportmanagement.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListenerForReports {

    private final ReportService reportService;


    @KafkaListener(topics = "gym-account-notifications", groupId = "notification")
    public void readNotificationFromBroker(String readData) throws JsonProcessingException {
        log.info("Received Data from Broker, Data : "+readData);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TrainingReportDTO reportDTO = objectMapper.readValue(readData, TrainingReportDTO.class);
        reportService.addNewReport(reportDTO);
        log.info("Exiting Kafka Listener");
    }

}
