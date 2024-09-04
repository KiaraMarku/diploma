package com.example.diploma.service;

import com.example.diploma.exception.UsernameAlreadyExistsException;
import com.example.diploma.dto.ProfessorRegistrationDTO;
import com.example.diploma.dto.StudentRegistrationDTO;
import com.example.diploma.entity.Professor;
import com.example.diploma.entity.Student;
import com.example.diploma.entity.security.Authority;
import com.example.diploma.entity.security.User;
import com.example.diploma.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RegistrationService {


    private UserRepository userRepository;

    private StudentRepository studentRepository;

    private ProfessorRepository professorRepository;

    private StudentGroupRepository groupRepository;
    private DepartmentRepository departmentRepository;

    private PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, StudentRepository studentRepository, ProfessorRepository professorRepository, PasswordEncoder passwordEncoder, StudentGroupRepository groupRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupRepository=groupRepository;
        this.departmentRepository = departmentRepository;
    }

    public void registerStudent(StudentRegistrationDTO registrationDto) throws ParseException {

        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }

            User user = new User();
            user.setUsername(registrationDto.getUsername());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setEnabled(true);

            user.addAuthority(new Authority(user,"ROLE_STUDENT"));
            userRepository.save(user);


            Student student = new Student();
            student.setUsername(registrationDto.getUsername());
            student.setFirstName(registrationDto.getFirstName());
            student.setLastName(registrationDto.getLastName());
            student.setEmail(registrationDto.getEmail());
            student.setCardId(registrationDto.getCardId());
            student.setGender(registrationDto.getGender());


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate;

            try {
                birthDate = dateFormat.parse(registrationDto.getBirthDate());
                student.setBirthDate(birthDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Integer group_id=registrationDto.getGroupId();
            student.setStudentGroup(groupRepository.findById(group_id).orElse(null));

            studentRepository.save(student);

    }

    public void registerProfessor(ProfessorRegistrationDTO registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }
            User user = new User();
            user.setUsername(registrationDto.getUsername());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setEnabled(true);
            user.addAuthority(new Authority(user,"ROLE_PROFESSOR"));
            userRepository.save(user);


            Professor professor = new Professor();
            professor.setUsername(registrationDto.getUsername());
            professor.setFirstName(registrationDto.getFirstName());
            professor.setLastName(registrationDto.getLastName());
            professor.setEmail(registrationDto.getEmail());
            professor.setCardId(registrationDto.getCardId());
            professor.setGender(registrationDto.getGender());


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate;

            try {
                birthDate = dateFormat.parse(registrationDto.getBirthDate());
                professor.setBirthDate(birthDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Integer department_id=registrationDto.getDepartmentId();

            professor.setDepartment(departmentRepository.findById(department_id).orElse(null));
            professor.setMajor(registrationDto.getMajor());
            professor.setTitle(registrationDto.getTitle());

            professorRepository.save(professor);

    }
}

