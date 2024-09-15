package com.example.diploma.service;

import com.example.diploma.entity.Notification;
import com.example.diploma.entity.Student;
import com.example.diploma.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final StudentService studentService;

    public NotificationService(NotificationRepository notificationRepository, StudentService studentService) {
        this.notificationRepository = notificationRepository;
        this.studentService = studentService;
    }

    public void createNotificationForStudent(Integer studentId, String message) {
        Student student = studentService.getStudentById(studentId);
        Notification notification = new Notification();
        notification.setStudent(student);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotificationsForStudent(Integer studentId) {
        return notificationRepository.findByStudentIdAndReadFalse(studentId);
    }

    public void markAllAsRead(Integer studentId) {
        List<Notification> unreadNotifications = notificationRepository.findByStudentIdAndReadFalse(studentId);
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }

    public int getUnreadNotificationCount(Integer studentId) {
        return notificationRepository.countByStudentIdAndReadFalse(studentId);
    }
}
