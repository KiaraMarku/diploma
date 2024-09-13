package com.example.diploma.controller;

import com.example.diploma.dto.ClassRequirementDTO;
import com.example.diploma.entity.Class;
import com.example.diploma.entity.ClassRequirement;
import com.example.diploma.entity.Student;
import com.example.diploma.entity.StudentClassRequirement;
import com.example.diploma.repository.StudentClassRequirementRepository;
import com.example.diploma.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentRequirementsController {

    private final AttendanceService seminarAttendanceService;
    private final LabAttendanceService labAttendanceService;
    private final ClassRequirementService classRequirementService;
    private final StudentService studentService;
    private final ClassService classService;



    public StudentRequirementsController(AttendanceService seminarAttendanceService, LabAttendanceService labAttendanceService, ClassRequirementService classRequirementService, StudentService studentService, ClassService classService, StudentClassRequirementRepository studentClassRequirementRepository) {
        this.seminarAttendanceService = seminarAttendanceService;
        this.labAttendanceService = labAttendanceService;
        this.classRequirementService = classRequirementService;
        this.studentService = studentService;
        this.classService = classService;

    }



    @GetMapping("/{studentId}/attendance/class/{classId}")
    public String viewAttendanceSummary(@PathVariable("classId") int classId,
                                        @PathVariable("studentId") int studentId,
                                        Model model) {
                // Seminar attendance
        Map<String, Object> seminarAttendance = seminarAttendanceService.calculateSeminarAttendance(studentId, classId);
        model.addAttribute("seminarAttendance", seminarAttendance);

        // Lab attendance
        Map<String, Object> labAttendance = labAttendanceService.calculateLabAttendance(studentId, classId);
        model.addAttribute("labAttendance", labAttendance);

        // Get class requirements
        List<ClassRequirement> classRequirements = classRequirementService.getClassRequirementsForStudent(studentId, classId);
        model.addAttribute("classRequirements", classRequirements);
        model.addAttribute("classRequirementDTO", new ClassRequirementDTO());

        List<StudentClassRequirement> studentRequirements = classRequirementService.findByStudentIdAndClassId(studentId,classId);
        model.addAttribute("studentRequirements", studentRequirements);
;
        // Get eligibility data
        Map<String, Object> eligibilityData = classRequirementService.calculateEligibility(studentId, classId);
        boolean isEligible = (boolean) eligibilityData.get("isEligible");
        String ineligibleReason = (String) eligibilityData.get("ineligibleReason");

        // Add eligibility data to model
        model.addAttribute("isEligible", isEligible);
        model.addAttribute("ineligibleReason", ineligibleReason);

        // Load other data as before (attendance, requirements, etc.)
        Student student = studentService.getStudentById(studentId);
        Class theClass = classService.getClassById(classId);
        model.addAttribute("student", student);
        model.addAttribute("class", theClass);

        return "student/performance-summary";
    }
    @PostMapping("/{studentId}/class/{classId}/saveRequirements")
    public String saveStudentRequirements(@PathVariable("studentId") int studentId,
                                          @PathVariable("classId") int classId,
                                          @ModelAttribute ClassRequirementDTO classRequirementDTO) {

        // Save the class requirements for the student
        classRequirementService.saveStudentRequirements(studentId, classId, classRequirementDTO.getRequirements());

        // Redirect back to the student performance summary
        return "redirect:/student/" + studentId + "/attendance/class/" + classId;
    }
}
