package com.example.diploma.repository;

import com.example.diploma.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByStudentIdAndReadFalse(Integer studentId);

    int countByStudentIdAndReadFalse(Integer studentId);
}
