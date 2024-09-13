package com.example.diploma.controller;

import com.example.diploma.dto.ExamDto;
import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.service.ClassService;
import com.example.diploma.service.ExamService;
import com.example.diploma.service.ProfessorService;
import com.example.diploma.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/professor/exams")
public class ProfessorExamController {

    private final ExamService examService;
    private final ProfessorService professorService;
    private final ClassService classService;
    private StudentService studentService;

    public ProfessorExamController(ExamService examService, ProfessorService professorService, ClassService classService, StudentService studentService) {
        this.examService = examService;
        this.professorService = professorService;
        this.classService = classService;
        this.studentService = studentService;
    }


    @GetMapping("/add/schedule")
    public String showScheduleForm(Model model) {
        Professor professor=professorService.getLoggedInProfessor();
        List<Class> classes = professorService.getClassesForProfessor(professor);
        List<ExamSeason> seasons = examService.getAllExamSeasons();
        model.addAttribute("examDto",new ExamDto());
        model.addAttribute("classes", classes);
        model.addAttribute("seasons", seasons);
        return "professor/exams/schedule-exam";
    }



    @PostMapping("/add/schedule")
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
            return "professor/exams/schedule-exam";
        }

        ExamSeason season = examService.getById(examDto.getSeasonId());

        if (examDto.getDate().isBefore(season.getStartDate()) || examDto.getDate().isAfter(season.getEndDate())) {
            model.addAttribute("error", "The selected date is outside the exam season.");
            model.addAttribute("classes", classes);
            model.addAttribute("seasons", seasons);
            return "professor/exams/schedule-exam";
        }

        boolean isDateAvailable = examService.isDateAvailable(examDto.getDate());
        if (!isDateAvailable) {
            model.addAttribute("error", "The selected date is already taken by another class.");
            model.addAttribute("classes", classes);
            model.addAttribute("seasons", seasons);
            return "professor/exams/schedule-exam"; // Return to the form view with errors
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
    @PostMapping("/schedule/delete")
    public String deleteExamSchedule(@RequestParam("examId") int examId) {
        examService.deleteById(examId);
        return "redirect:/exams/schedule/view";
    }

    //method to view exams that are finished
    @GetMapping("/past/view")
    public String viewPastExams(Model model) {
        Professor professor = professorService.getLoggedInProfessor();
        List<Exam> pastExams = examService.getPastExamsForProfessor(professor);
        model.addAttribute("pastExams", pastExams);
        return "professor/exams/past-exams";
    }


    //view student list for exam and scores
    @GetMapping("/{examId}/students")
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

        return "professor/exams/exam-student-list";
    }
    @GetMapping("/{examId}/student/{studentId}/edit")
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
        return "professor/exams/grade-exam";
    }


    @PostMapping("/{examId}/student/{studentId}/edit")
    public String updateExamCopy(@PathVariable("studentId") int studentId,
                                 @PathVariable("examId") int examId,
                                 @RequestParam("status") String status,
                                 @RequestParam("score") int score) {

        examService.saveOrUpdateExamCopy(studentId, examId, status, score);

        return "redirect:/professor/exams/" + examId + "/students";
    }







}
