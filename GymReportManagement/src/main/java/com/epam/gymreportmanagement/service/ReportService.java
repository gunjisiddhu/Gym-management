package com.epam.gymreportmanagement.service;

import com.epam.gymreportmanagement.dto.ReportDTO;
import com.epam.gymreportmanagement.dto.TrainingReportDTO;
import com.epam.gymreportmanagement.exception.ReportException;

public interface ReportService {

    String ADD_NEW_REPORT = "addNewReport";
    String GET_REPORT = "getReport";
    void addNewReport(TrainingReportDTO trainingReport);
    ReportDTO getReport(String username) throws ReportException;
}
