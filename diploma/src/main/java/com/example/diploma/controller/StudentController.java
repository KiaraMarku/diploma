package com.example.diploma.controller;

import com.example.diploma.dto.ClassRequirementDTO;
import com.example.diploma.entity.Class;
import com.example.diploma.entity.ClassRequirement;
import com.example.diploma.entity.Student;
import com.example.diploma.entity.StudentClassRequirement;
import com.example.diploma.repository.StudentClassRequirementRepository;
import com.example.diploma.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private  Student student;
    private AttendanceService seminarAttendanceService;
    private LabAttendanceService labAttendanceService;
    private ClassRequirementService classRequirementService;


    @Autowired
    public StudentController(StudentService studentService, AttendanceService seminarAttendanceService, LabAttendanceService labAttendanceService, StudentClassRequirementRepository studentClassRequirementRepository, ClassRequirementService classRequirementService, ClassService classService, ClassRequirementService classRequirementService1) {
        this.studentService = studentService;

        this.seminarAttendanceService = seminarAttendanceService;
        this.labAttendanceService = labAttendanceService;
        this.classRequirementService = classRequirementService1;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        this.student=studentService.getLoggedInStudent();
        model.addAttribute("student", student);
        return "student/student-profile";
    }

    @GetMapping("/classes")
    public String viewClasses(Model model) {
        this.student = studentService.getLoggedInStudent();
        Set<Class> classes = studentService.getClassesForStudent(student);
        model.addAttribute("classes", classes);
        return "student/student-classes";
    }

    @GetMapping("/summary/{classId}")
    public String viewAttendanceSummary(@PathVariable("classId") int classId, Model model) {
        this.student=studentService.getLoggedInStudent();
        // Get seminar attendance details
        Map<String, Object> seminarAttendance = seminarAttendanceService.calculateSeminarAttendance(student.getId(), classId);
        model.addAttribute("seminarAttendance", seminarAttendance);

        // Get lab attendance details
        Map<String, Object> labAttendance = labAttendanceService.calculateLabAttendance(student.getId(), classId);
        model.addAttribute("labAttendance", labAttendance);


        List<StudentClassRequirement> studentRequirements = classRequirementService.findByStudentIdAndClassId(student.getId(),classId);
        model.addAttribute("studentRequirements", studentRequirements);

        // Get class requirements
        List<ClassRequirement> classRequirements = classRequirementService.getClassRequirementsForStudent(student.getId(), classId);
        model.addAttribute("classRequirements", classRequirements);
        model.addAttribute("classRequirementDTO", new ClassRequirementDTO());


        // Calculate eligibility
        boolean attendanceEligible = (boolean) seminarAttendance.get("eligible") && (boolean) labAttendance.get("eligible");
        boolean requirementsEligible = true;
        StringBuilder ineligibleReason = new StringBuilder();

        for (ClassRequirement requirement : classRequirements) {
            StudentClassRequirement studentRequirement = studentRequirements.stream()
                    .filter(req -> req.getClassRequirement().getId().equals(requirement.getId()))
                    .findFirst()
                    .orElse(null);

            if (studentRequirement == null || studentRequirement.getScore() == 0 || (requirement.getPassFail() && studentRequirement.getScore() == 0)) {
                requirementsEligible = false;
                ineligibleReason.append("Failed ").append(requirement.getRequirementType()).append(". ");
            }
        }

        // Determine final eligibility status
        boolean isEligible = attendanceEligible && requirementsEligible;
        if (!attendanceEligible) {
            ineligibleReason.append("Attendance requirements not met. ");
        }

        // Add eligibility to model
        model.addAttribute("isEligible", isEligible);
        model.addAttribute("ineligibleReason", ineligibleReason.toString());

        return "student/performance-summary";
    }


}

