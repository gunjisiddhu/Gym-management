package com.epam.gymserviceservice.repository;

import com.epam.gymserviceservice.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends MongoRepository<Notification, String> {
}
