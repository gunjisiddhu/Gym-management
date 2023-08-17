package com.epam.gymreportmanagement.service;


import com.epam.gymreportmanagement.dto.ReportDTO;
import com.epam.gymreportmanagement.dto.TrainingReportDTO;
import com.epam.gymreportmanagement.exception.ReportException;
import com.epam.gymreportmanagement.model.Report;
import com.epam.gymreportmanagement.repository.TrainingReportRepository;
import com.epam.gymreportmanagement.util.StringConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService{


    private final TrainingReportRepository trainingReportRepository;

    @Transactional
    public void addNewReport(TrainingReportDTO trainingReport){
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), ADD_NEW_REPORT, this.getClass().getName(), trainingReport.toString());
        Report report = trainingReportRepository.findById(trainingReport.getTrainerUsername())
                .orElseGet(() -> new Report(
                        trainingReport.getTrainerUsername(),
                        trainingReport.getTrainerFirstName(),
                        trainingReport.getTrainerLastName(),
                        trainingReport.isTrainerStatus(),
                        null
                ));

        if (report.getTrainingSummaries() == null) {
            report.setTrainingSummaries(new HashMap<>());
        }

        LocalDate trainingDate = trainingReport.getTrainingDate();

        long year = trainingDate.getYear();
        long month = trainingDate.getMonthValue();
        long day = trainingDate.getDayOfMonth();
        long trainingDuration = trainingReport.getTrainingDuration();

        report.getTrainingSummaries()
                .computeIfAbsent(year, k -> new HashMap<>())
                .computeIfAbsent(month, k -> new HashMap<>())
                .computeIfAbsent(day, k -> new HashMap<>())
                .put(trainingDate.toString(), trainingDuration);


        trainingReportRepository.save(report);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), ADD_NEW_REPORT, this.getClass().getName());
    }


    public ReportDTO getReport(String username) throws ReportException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_REPORT, this.getClass().getName(), username);
        Report report = trainingReportRepository.findById(username).orElseThrow(() -> new ReportException("No reports found for the given user"));
        ReportDTO reportDTO = ReportDTO.builder()
                .trainerUsername(report.getTrainerUsername())
                .trainerFirstName(report.getTrainerFirstName())
                .trainerLastName(report.getTrainerLastName())
                .trainerStatus(report.isTrainerStatus())
                .trainingSummaries(report.getTrainingSummaries())
                .build();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_REPORT, this.getClass().getName());
        return reportDTO;
    }

}
