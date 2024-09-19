package com.example.diploma.service;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExamService {

    private ExamRepository examRepository;
    private StudentRepository studentRepository;
    private ExamSeasonRepository examSeasonRepository;
    private ExamCopyRepository examCopyRepository;
    private ClassRequirementService requirementService;
    private ScheduleRepository scheduleRepository;
    private NotificationService notificationService;


    public ExamService(ExamRepository examRepository, StudentRepository studentRepository, ExamSeasonRepository examSeasonRepository, ExamCopyRepository examCopyRepository, AttendanceService attendanceService, ClassRequirementService requirementService, ScheduleRepository scheduleRepository, NotificationService notificationService) {
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.examSeasonRepository = examSeasonRepository;
        this.examCopyRepository = examCopyRepository;
        this.requirementService = requirementService;
        this.scheduleRepository = scheduleRepository;
        this.notificationService = notificationService;
    }


    public List<ExamSeason> getAllExamSeasons() {
        return examSeasonRepository.findAll();
    }


    public ExamSeason getById(int seasonId) {
        return examSeasonRepository.findById(seasonId).orElse(null);
    }


    public boolean isDateAvailable(LocalDate date) {
        List<Exam> examsOnDate = examRepository.findByDate(date);
        return examsOnDate.isEmpty(); // Returns true if no exam is scheduled on this date
    }


    public List<Exam> getExamsForSeason(int seasonId) {
        return examRepository.findByExamSeasonIdOrderByDate(seasonId);
    }


    public void saveExam(Exam exam) {
        examRepository.save(exam);
    }

    public boolean existsByClassIdAndExamSeasonId(int classId, int seasonId) {
        return examRepository.existsByTheClassIdAndExamSeasonId(classId, seasonId);
    }

    public void deleteById(int examId) {
        examRepository.deleteById(examId);
    }

    public List<Exam> getPastExamsForProfessor(Professor professor) {
        // Fetch exams for the professor and filter by date
        LocalDate currentDate = LocalDate.now();
        return examRepository.findByProfessorIdAndDateBefore(professor.getId(), currentDate);
    }

    public List<Student> getStudentsForExam(int examId) {
        Class theClass = examRepository.findById(examId).orElse(null).getTheClass();
        List<StudentGroup> studentGroups = scheduleRepository.findGroupsByClassId(theClass.getId());
        List<Student> eligibleStudents = new ArrayList<>();

        for (StudentGroup group : studentGroups) {
            List<Student> students = studentRepository.findStudentsByStudentGroupId(group.getId());

            for (Student student : students) {
                Map<String, Object> eligibilityData = requirementService.calculateEligibility(student.getId(), theClass.getId());
                boolean isEligible = (boolean) eligibilityData.get("isEligible");


                if (isEligible) {
                    eligibleStudents.add(student);
                }
            }
        }

        return eligibleStudents;
    }


    public String calculateGrade(int points) {
        if (points < 45) return "4";
        if (points <= 55) return "5";
        if (points <= 65) return "6";
        if (points <= 75) return "7";
        if (points <= 85) return "8";
        if (points <= 95) return "9";
        return "10";
    }


    public ExamCopy getExamCopyForStudentAndExam(int studentId, int examId) {
        return examCopyRepository.findByStudentIdAndExamId(studentId, examId).orElse(null);
    }

    public Exam getExamById(int examId) {
        return examRepository.findById(examId).orElse(null);
    }

    public ExamCopy saveOrUpdateExamCopy(int studentId, int examId, String status, int score, MultipartFile examFile) {
        Exam exam = examRepository.findById(examId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);
        ExamCopy examCopy = examCopyRepository.findByStudentIdAndExamId(studentId, examId).orElse(new ExamCopy());
        examCopy.setStatus(status);
        examCopy.setScore(score);
        examCopy.setGrade(calculateGrade(score));
        examCopy.setExam(exam);
        examCopy.setStudent(student);


        if (examFile != null && !examFile.isEmpty()) {
            try {
                examCopy.setFileName(examFile.getOriginalFilename());
                examCopy.setFileType(examFile.getContentType());
                examCopy.setData(examFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        examCopyRepository.save(examCopy);
        String message = "Your exam for " + exam.getTheClass().getName() + " has been graded.";
        notificationService.createNotificationForStudent(studentId, message);
        return examCopy;
    }


    public List<ExamCopy> getGradedExamsForStudent(int studentId) {
        return examCopyRepository.findGradedExamsByStudentId(studentId);
    }

    public ExamCopy getExamCopyById(int examCopyId) {
        return examCopyRepository.findById(examCopyId).orElse(null);
    }

    public ExamCopy getExamCopyForStudentAndClass(int studentId, int classId) {
        return examCopyRepository.findByStudentIdAndClassId(studentId, classId);
    }
}
