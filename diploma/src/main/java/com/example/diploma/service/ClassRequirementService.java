package com.example.diploma.service;

import com.example.diploma.entity.ClassRequirement;
import com.example.diploma.entity.StudentClassRequirement;
import com.example.diploma.repository.ClassRequirementRepository;
import com.example.diploma.repository.StudentClassRequirementRepository;
import com.example.diploma.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassRequirementService {

    private final StudentRepository studentRepository;
    private final ClassRequirementRepository classRequirementRepository;
    private final StudentClassRequirementRepository studentClassRequirementRepository;
    private LabAttendanceService labAttendanceService;
    private AttendanceService seminarAttendanceService;

    public ClassRequirementService(StudentRepository studentRepository, ClassRequirementRepository classRequirementRepository, StudentClassRequirementRepository studentClassRequirementRepository, LabAttendanceService labAttendanceService, AttendanceService seminarAttendanceService) {
        this.studentRepository = studentRepository;
        this.classRequirementRepository = classRequirementRepository;
        this.studentClassRequirementRepository = studentClassRequirementRepository;
        this.labAttendanceService = labAttendanceService;
        this.seminarAttendanceService = seminarAttendanceService;
    }

    public List<ClassRequirement> getClassRequirementsForStudent(int studentId, int classId) {

        return classRequirementRepository.findByTheClassId(classId);
    }

    public List<StudentClassRequirement> findByStudentIdAndClassId(int studentId,int classId){
        return studentClassRequirementRepository.findByStudentIdAndClassId(studentId,classId);
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
}
