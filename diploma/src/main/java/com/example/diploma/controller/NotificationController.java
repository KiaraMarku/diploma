package com.example.diploma.controller;

import com.example.diploma.entity.Notification;
import com.example.diploma.entity.Student;
import com.example.diploma.service.NotificationService;
import com.example.diploma.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/student")
public class NotificationController {

    private final NotificationService notificationService;
    private StudentService studentService;

    public NotificationController(NotificationService notificationService, StudentService studentService) {
        this.notificationService = notificationService;
        this.studentService = studentService;
    }

    @GetMapping("/notifications")
    public String viewNotifications(Model model) {
        Student student = studentService.getLoggedInStudent();
        List<Notification> notifications = notificationService.getUnreadNotificationsForStudent(student.getId());
        model.addAttribute("notifications", notifications);
        return "student/notifications";
    }

    @PostMapping("/notifications/markAsRead")
    public String markNotificationsAsRead() {
        Student student = studentService.getLoggedInStudent();
        notificationService.markAllAsRead(student.getId());
        return "redirect:/student/notifications";
    }

    @GetMapping("/notifications/count")
    @ResponseBody
    public int getUnreadNotificationCount() {
        Student student = studentService.getLoggedInStudent();
        return notificationService.getUnreadNotificationCount(student.getId());
    }
}
