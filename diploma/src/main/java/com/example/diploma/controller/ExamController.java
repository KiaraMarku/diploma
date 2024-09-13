package com.example.diploma.controller;

import com.example.diploma.dto.ExamDto;
import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.service.ClassService;
import com.example.diploma.service.ExamService;
import com.example.diploma.service.ProfessorService;
import com.example.diploma.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExamController {

    private final ExamService examService;
    private final ProfessorService professorService;
    private final ClassService classService;
    private StudentService studentService;

    public ExamController(ExamService examService, ProfessorService professorService, ClassService classService, StudentService studentService) {
        this.examService = examService;
        this.professorService = professorService;
        this.classService = classService;
        this.studentService = studentService;
    }


    @GetMapping("exams")
    @ResponseBody
    public List<Exam> getExamsForSeason(@RequestParam("seasonId") int seasonId) {
        return examService.getExamsForSeason(seasonId);
    }


    @GetMapping("exams/add/schedule")
    public String showScheduleForm(Model model) {
        Professor professor=professorService.getLoggedInProfessor();
        List<Class> classes = professorService.getClassesForProfessor(professor);
        List<ExamSeason> seasons = examService.getAllExamSeasons();
        model.addAttribute("examDto",new ExamDto());
        model.addAttribute("classes", classes);
        model.addAttribute("seasons", seasons);
        return "exams/schedule-exam";
    }


    @GetMapping("exams/schedule/view")
    public String viewExamSchedules(Model model) {
        List<ExamSeason> examSeasons = examService.getAllExamSeasons();
        model.addAttribute("examSeasons", examSeasons);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst().get().getAuthority();
        model.addAttribute("userRole", role);
        return "exams/schedule";
    }


    @PostMapping("exams/schedule")
    public String scheduleExam(@Valid @ModelAttribute("examDto") ExamDto examDto,
                               Model model) {
        Professor professor = professorService.getLoggedInProfessor();
        List<Class> classes = professorService.getClassesForProfessor(professor);
        List<ExamSeason> seasons = examService.getAllExamSeasons();



        boolean exists = examService.existsByClassIdAndExamSeasonId(examDto.getClassId(), examDto.getSeasonId());
        if (exists) {
            model.addAttribute("error", "An exam is already scheduled for this class in the selected exam season.");
            model.addAttribute("classes", classes);
            model.addAttribute("seasons", seasons);
            return "exams/schedule-exam";
        }

        ExamSeason season = examService.getById(examDto.getSeasonId());

        if (examDto.getDate().isBefore(season.getStartDate()) || examDto.getDate().isAfter(season.getEndDate())) {
            model.addAttribute("error", "The selected date is outside the exam season.");
            model.addAttribute("classes", classes);
            model.addAttribute("seasons", seasons);
            return "exams/schedule-exam";
        }

        boolean isDateAvailable = examService.isDateAvailable(examDto.getDate());
        if (!isDateAvailable) {
            model.addAttribute("error", "The selected date is already taken by another class.");
            model.addAttribute("classes", classes);
            model.addAttribute("seasons", seasons);
            return "exams/schedule-exam"; // Return to the form view with errors
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
    @PostMapping("exams/schedule/delete")
    public String deleteExamSchedule(@RequestParam("examId") int examId) {
        examService.deleteById(examId);
        return "redirect:/exams/schedule/view";
    }

    //method to view exams that are finished
    @GetMapping("exams/professor/view")
    public String viewPastExams(Model model) {
        Professor professor = professorService.getLoggedInProfessor();
        List<Exam> pastExams = examService.getPastExamsForProfessor(professor);
        model.addAttribute("pastExams", pastExams);
        return "exams/past-exams";
    }


    //view student list for exam and scores
    @GetMapping("/professor/exam/{examId}/students")
    public String viewExamStudents(@PathVariable("examId") int examId, Model model) {
        List<Student> eligibleStudents = examService.getStudentsForExam(examId);
        Exam exam = examService.getExamById(examId);

        Map<Student, ExamCopy> studentExamMap = new HashMap<>();
        for (Student student : eligibleStudents) {
            ExamCopy examCopy = examService.getExamCopyForStudentAndExam(student.getId(), examId);
            studentExamMap.put(student, examCopy);
        }

        // Add the map to the model
        model.addAttribute("studentExamMap", studentExamMap);
        model.addAttribute("exam", exam);

        return "exams/exam-student-list";
    }
    @GetMapping("/exams/{examId}/student/{studentId}/edit")
    public String showEditExamPage(@PathVariable("examId") int examId, @PathVariable("studentId") int studentId, Model model) {
        ExamCopy examCopy = examService.getExamCopyForStudentAndExam(studentId, examId);

        // If no exam copy exists, create a new one with default values
        if (examCopy == null) {
            examCopy = new ExamCopy();
            examCopy.setStudent(studentService.getStudentById(studentId));
            examCopy.setExam(examService.getExamById(examId));
            examCopy.setStatus("Absent"); // Default to absent if not graded yet
            examCopy.setScore(0); // Default score
        }

        model.addAttribute("examCopy", examCopy);
        return "exams/grade-exam";
    }


    @PostMapping("/exams/{examId}/student/{studentId}/edit")
    public String updateExamCopy(@PathVariable("studentId") int studentId,
                                 @PathVariable("examId") int examId,
                                 @RequestParam("status") String status,
                                 @RequestParam("score") int score) {

        examService.saveOrUpdateExamCopy(studentId, examId, status, score);

        return "redirect:/professor/exam/" + examId + "/students";
    }

    @GetMapping("student/exams/graded")
    public String viewGradedExams(Model model) {
        Student student = studentService.getLoggedInStudent();
        List<ExamCopy> gradedExams = examService.getGradedExamsForStudent(student.getId());

        model.addAttribute("gradedExams", gradedExams);
        model.addAttribute("student", student);
        return "student/graded-exams";
    }






}
