package com.epam.gymreportmanagement.repository;


import com.epam.gymreportmanagement.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingReportRepository extends MongoRepository<Report, String> {
}
