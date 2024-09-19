package com.example.diploma.service;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.repository.ClassRequirementRepository;
import com.example.diploma.repository.StudentClassGradeRepository;
import com.example.diploma.repository.StudentClassRequirementRepository;
import com.example.diploma.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClassRequirementService {

    private final StudentRepository studentRepository;
    private final ClassRequirementRepository classRequirementRepository;
    private final StudentClassRequirementRepository studentClassRequirementRepository;
    private final LabAttendanceService labAttendanceService;
    private final AttendanceService seminarAttendanceService;
    private final StudentClassGradeRepository studentClassGradeRepository;
    private final StudentService studentService;
    private final ClassService classService;

    public ClassRequirementService(StudentRepository studentRepository, ClassRequirementRepository classRequirementRepository, StudentClassRequirementRepository studentClassRequirementRepository, LabAttendanceService labAttendanceService, AttendanceService seminarAttendanceService, StudentClassGradeRepository studentClassGradeRepository, StudentService studentService, ClassService classService) {
        this.studentRepository = studentRepository;
        this.classRequirementRepository = classRequirementRepository;
        this.studentClassRequirementRepository = studentClassRequirementRepository;
        this.labAttendanceService = labAttendanceService;
        this.seminarAttendanceService = seminarAttendanceService;
        this.studentClassGradeRepository = studentClassGradeRepository;
        this.studentService = studentService;
        this.classService = classService;
    }

    public List<ClassRequirement> getClassRequirementsForStudent(int studentId, int classId) {

        return classRequirementRepository.findByTheClassId(classId);
    }

    public List<StudentClassRequirement> findByStudentIdAndClassId(int studentId, int classId) {
        return studentClassRequirementRepository.findByStudentIdAndClassId(studentId, classId);
    }

    public void saveStudentRequirements(int studentId, int classId, Map<Integer, Integer> requirements) {

        for (Map.Entry<Integer, Integer> entry : requirements.entrySet()) {
            Integer requirementId = entry.getKey();
            Integer score = entry.getValue();

            ClassRequirement classRequirement = classRequirementRepository.findById(requirementId).orElse(null);

            if (classRequirement != null) {

                StudentClassRequirement studentRequirement = studentClassRequirementRepository
                        .findByStudentIdAndClassRequirementId(studentId, classRequirement.getId())
                        .orElse(new StudentClassRequirement());


                studentRequirement.setStudent(studentRepository.findById(studentId).orElse(null));
                studentRequirement.setClassRequirement(classRequirement);


                if (classRequirement.getPassFail()) {
                    studentRequirement.setScore(score);
                } else {
                    studentRequirement.setScore(Math.min(score, classRequirement.getTotalPoints()));
                }

                studentClassRequirementRepository.save(studentRequirement);
            }
        }
    }

    public Map<String, Object> calculateEligibility(int studentId, int classId) {
        Map<String, Object> eligibilityMap = new HashMap<>();

        // Calculate Seminar and Lab attendance eligibility
        Map<String, Object> seminarAttendance = seminarAttendanceService.calculateSeminarAttendance(studentId, classId);
        Map<String, Object> labAttendance = labAttendanceService.calculateLabAttendance(studentId, classId);

        // Get class requirements
        List<ClassRequirement> classRequirements = getClassRequirementsForStudent(studentId, classId);
        List<StudentClassRequirement> studentRequirements = studentClassRequirementRepository.findByStudentIdAndClassId(studentId, classId);

        // Check if attendance requirements are met
        boolean attendanceEligible = (boolean) seminarAttendance.get("eligible") && (boolean) labAttendance.get("eligible");

        // Check if class requirements are met
        boolean requirementsEligible = true;
        StringBuilder ineligibleReason = new StringBuilder();

        for (ClassRequirement requirement : classRequirements) {
            StudentClassRequirement studentRequirement = studentRequirements.stream()
                    .filter(req -> req.getClassRequirement().getId().equals(requirement.getId()))
                    .findFirst()
                    .orElse(null);

            if (studentRequirement == null || studentRequirement.getScore() == 0 ||
                    (requirement.getPassFail() && studentRequirement.getScore() == 0)) {
                requirementsEligible = false;
                ineligibleReason.append("Failed ").append(requirement.getRequirementType()).append(". ");
            }
        }

        // Combine attendance and requirements eligibility
        boolean isEligible = attendanceEligible && requirementsEligible;
        if (!attendanceEligible) {
            ineligibleReason.append("Attendance requirements not met. ");
        }

        eligibilityMap.put("isEligible", isEligible);
        eligibilityMap.put("ineligibleReason", ineligibleReason.toString());

        return eligibilityMap;
    }


    public void calculateAndSaveFinalGrade(int studentId, int classId, int examScore) {

        List<StudentClassRequirement> requirements = studentClassRequirementRepository.findByStudentIdAndClassId(studentId, classId);

        int totalRequirementPoints = 0;
        int totalObtainedPoints = 0;

        for (StudentClassRequirement requirement : requirements) {
            ClassRequirement classRequirement = requirement.getClassRequirement();

            totalObtainedPoints += requirement.getScore();
            totalRequirementPoints += classRequirement.getTotalPoints();
        }

        int totalOverallPoints = 100;
        int totalExamPoints = totalOverallPoints - totalRequirementPoints;

        double examWeight = (double) totalExamPoints / totalOverallPoints;
        int finalScore = (int) ((examScore * examWeight) + totalObtainedPoints);

        int finalGrade = calculateGrade(finalScore);
        String status = finalGrade >= 5 ? "Passed" : "Failed";


        Student student = studentService.getStudentById(studentId);
        Class theClass = classService.getClassById(classId);

        StudentClassGrade studentClassGrade = studentClassGradeRepository.findByStudentIdAndTheClassId(studentId, classId)
                .orElse(new StudentClassGrade());
        studentClassGrade.setStudent(student);
        studentClassGrade.setTheClass(theClass);
        studentClassGrade.setFinalScore(finalScore);
        studentClassGrade.setFinalGrade(finalGrade);
        studentClassGrade.setStatus(status);

        studentClassGradeRepository.save(studentClassGrade);

    }

    public Optional<StudentClassGrade> getFinalGradeForClass(int studentId, int classId) {
        return studentClassGradeRepository.findByStudentIdAndTheClassId(studentId, classId);
    }

    public int calculateGrade(double finalScore) {
        if (finalScore < 45) {
            return 4;
        } else if (finalScore < 55) {
            return 5;
        } else if (finalScore < 65) {
            return 6;
        } else if (finalScore < 75) {
            return 7;
        } else if (finalScore < 85) {
            return 8;
        } else if (finalScore < 95) {
            return 9;
        } else {
            return 10;
        }
    }

}
