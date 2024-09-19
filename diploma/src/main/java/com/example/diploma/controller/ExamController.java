package com.example.diploma.controller;

import com.example.diploma.dto.ExamDto;
import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.service.ClassService;
import com.example.diploma.service.ExamService;
import com.example.diploma.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ExamController {

    private final ExamService examService;

    private final StudentService studentService;
    private final ClassService classService;

    public ExamController(ExamService examService, StudentService studentService, ClassService classService) {
        this.examService = examService;
        this.studentService = studentService;
        this.classService = classService;
    }


    @GetMapping("exams")
    @ResponseBody
    public List<Exam> getExamsForSeason(@RequestParam("seasonId") int seasonId) {
        return examService.getExamsForSeason(seasonId);
    }


    @GetMapping("exams/schedule/view")
    public String viewExamSchedules(Model model) {
        List<ExamSeason> examSeasons = examService.getAllExamSeasons();
        model.addAttribute("examSeasons", examSeasons);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst().get().getAuthority();
        model.addAttribute("userRole", role);
        return "common/exams-schedule";
    }

    @GetMapping("student/exams/graded")
    public String viewGradedExams(Model model) {
        Student student = studentService.getLoggedInStudent();
        List<ExamCopy> gradedExams = examService.getGradedExamsForStudent(student.getId());

        model.addAttribute("gradedExams", gradedExams);
        model.addAttribute("student", student);
        return "student/graded-exams";
    }

    @GetMapping("/student/exam/{examCopyId}/download")
    public ResponseEntity<byte[]> downloadExamCopy(@PathVariable("examCopyId") int examCopyId) {
        ExamCopy examCopy = examService.getExamCopyById(examCopyId);

        if (examCopy != null && examCopy.getData() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(examCopy.getFileType()));
            headers.setContentDispositionFormData("attachment", examCopy.getFileName());

            return new ResponseEntity<>(examCopy.getData(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }

    @GetMapping("/admin/exams/add/schedule")
    public String showScheduleForm(Model model) {

        List<Class> classes = classService.getAllClasses();
        List<ExamSeason> seasons = examService.getAllExamSeasons();
        model.addAttribute("examDto", new ExamDto());
        model.addAttribute("classes", classes);
        model.addAttribute("seasons", seasons);
        return "admin/schedule-exam";
    }

    @PostMapping("/admin/exams/add/schedule")
    public String scheduleExam(@Valid @ModelAttribute("examDto") ExamDto examDto,
                               Model model) {
        List<Class> classes = classService.getAllClasses();
        List<ExamSeason> seasons = examService.getAllExamSeasons();


        boolean exists = examService.existsByClassIdAndExamSeasonId(examDto.getClassId(), examDto.getSeasonId());
        if (exists) {
            model.addAttribute("error", "An exam is already scheduled for this class in the selected exam season.");
            model.addAttribute("classes", classes);
            model.addAttribute("seasons", seasons);
            return "admin/schedule-exam";
        }

        ExamSeason season = examService.getById(examDto.getSeasonId());

        if (examDto.getDate().isBefore(season.getStartDate()) || examDto.getDate().isAfter(season.getEndDate())) {
            model.addAttribute("error", "The selected date is outside the exam season.");
            model.addAttribute("classes", classes);
            model.addAttribute("seasons", seasons);
            return "admin/schedule-exam";
        }

        boolean isDateAvailable = examService.isDateAvailable(examDto.getDate());
        if (!isDateAvailable) {
            model.addAttribute("error", "The selected date is already taken by another class.");
            model.addAttribute("classes", classes);
            model.addAttribute("seasons", seasons);
            return "admin/schedule-exam"; // Return to the form view with errors
        }


        // Save the exam
        Exam exam = new Exam();
        exam.setTheClass(classService.getClassById(examDto.getClassId()));
        exam.setExamSeason(season);
        exam.setDate(examDto.getDate());
        exam.setStartTime(examDto.getStartTime());
        exam.setEndTime(examDto.getEndTime());
        exam.setExamHall(examDto.getExamHall());
        examService.saveExam(exam);

        return "redirect:/exams/schedule/view";
    }

}