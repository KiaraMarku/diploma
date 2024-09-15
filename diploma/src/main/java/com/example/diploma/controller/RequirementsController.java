package com.example.diploma.controller;

import com.example.diploma.dto.ClassRequirementDTO;
import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.repository.StudentClassRequirementRepository;
import com.example.diploma.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class RequirementsController {

    private final AttendanceService seminarAttendanceService;
    private final LabAttendanceService labAttendanceService;
    private final ClassRequirementService classRequirementService;
    private final StudentService studentService;
    private final ClassService classService;
    private final ExamService examService;


    public RequirementsController(AttendanceService seminarAttendanceService, LabAttendanceService labAttendanceService, ClassRequirementService classRequirementService, StudentService studentService, ClassService classService, StudentClassRequirementRepository studentClassRequirementRepository, ExamService examService) {
        this.seminarAttendanceService = seminarAttendanceService;
        this.labAttendanceService = labAttendanceService;
        this.classRequirementService = classRequirementService;
        this.studentService = studentService;
        this.classService = classService;

        this.examService = examService;
    }


    @GetMapping("/professor/student/{studentId}/performance/class/{classId}")
    public String viewPerformanceSummary(@PathVariable("classId") int classId,
                                        @PathVariable("studentId") int studentId,
                                        Model model) {


        calculatePerformance(classId,studentId,model);
        // Get class requirements
        List<ClassRequirement> classRequirements = classRequirementService.getClassRequirementsForStudent(studentId, classId);
        model.addAttribute("classRequirements", classRequirements);
        model.addAttribute("classRequirementDTO", new ClassRequirementDTO());
         return "common/performance-summary";
    }
    @PostMapping("/professor/student/{studentId}/class/{classId}/saveRequirements")
    public String saveStudentRequirements(@PathVariable("studentId") int studentId,
                                          @PathVariable("classId") int classId,
                                          @ModelAttribute ClassRequirementDTO classRequirementDTO) {


        classRequirementService.saveStudentRequirements(studentId, classId, classRequirementDTO.getRequirements());


        return "redirect:/student/" + studentId + "/performance/class/" + classId;
    }

    @GetMapping("/student/summary/{classId}")
    public String viewPerformanceSummary(@PathVariable("classId") int classId, Model model) {
        Student student=studentService.getLoggedInStudent();
        calculatePerformance(classId,student.getId(),model);

        return "common/performance-summary";
    }



    private void calculatePerformance(int classId,int studentId,Model model){
        // Seminar attendance
        Map<String, Object> seminarAttendance = seminarAttendanceService.calculateSeminarAttendance(studentId, classId);
        model.addAttribute("seminarAttendance", seminarAttendance);

        // Lab attendance
        Map<String, Object> labAttendance = labAttendanceService.calculateLabAttendance(studentId, classId);
        model.addAttribute("labAttendance", labAttendance);


        List<StudentClassRequirement> studentRequirements = classRequirementService.findByStudentIdAndClassId(studentId,classId);
        model.addAttribute("studentRequirements", studentRequirements);

        // Get eligibility data
        Map<String, Object> eligibilityData = classRequirementService.calculateEligibility(studentId, classId);
        boolean isEligible = (boolean) eligibilityData.get("isEligible");
        String ineligibleReason = (String) eligibilityData.get("ineligibleReason");

        // Add eligibility data to model
        model.addAttribute("isEligible", isEligible);
        model.addAttribute("ineligibleReason", ineligibleReason);

        // Get graded exam copy if it exists
        ExamCopy examCopy = examService.getExamCopyForStudentAndClass(studentId, classId);
        model.addAttribute("examCopy", examCopy);
        StudentClassGrade studentClassGrade=classRequirementService.getFinalGradeForClass(studentId,classId).orElse(null);

        // Check if the student has a graded exam and calculate the final grade
        if (examCopy != null && studentClassGrade!=null) {
            double finalScore = studentClassGrade.getFinalScore();
            int finalGrade= (int) studentClassGrade.getFinalGrade();
            model.addAttribute("finalScore",finalScore);
            model.addAttribute("finalGrade", finalGrade);
            model.addAttribute("hasExamCopy", true);

            // Determine pass or fail based on the final grade
            boolean hasPassed = finalGrade >= 5;
            model.addAttribute("hasPassed", hasPassed);
        } else {
            model.addAttribute("hasExamCopy", false);
        }


        Student student = studentService.getStudentById(studentId);
        Class theClass = classService.getClassById(classId);
        model.addAttribute("student", student);
        model.addAttribute("class", theClass);

    }


}
