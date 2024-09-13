package com.example.diploma.controller;

import com.example.diploma.dto.ProfessorRegistrationDTO;
import com.example.diploma.dto.StudentRegistrationDTO;
import com.example.diploma.entity.Department;
import com.example.diploma.entity.Professor;
import com.example.diploma.entity.Student;
import com.example.diploma.entity.StudentGroup;
import com.example.diploma.exception.UsernameAlreadyExistsException;
import com.example.diploma.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private RegistrationService registrationService;
    private GroupService groupService;
    private DepartmentService departmentService;
    private StudentService studentService;
    private ProfessorService professorService;
    private GroupService studentGroupService;

    public AdminController(RegistrationService registrationService, GroupService groupService, DepartmentService departmentService, StudentService studentService, ProfessorService professorService, GroupService studentGroupService) {
        this.registrationService = registrationService;
        this.groupService = groupService;
        this.departmentService = departmentService;
        this.studentService = studentService;
        this.professorService = professorService;
        this.studentGroupService = studentGroupService;
    }


    @GetMapping("/register/studentForm")
       public String showStudentRegistrationForm(Model model) {
        model.addAttribute("student", new StudentRegistrationDTO());
        model.addAttribute("groups", groupService.findAll());
        return "admin/student-registration";
    }

    @PostMapping("/register/student")
    public String registerStudent(@ModelAttribute("student") @Valid StudentRegistrationDTO registrationDto,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("groups", groupService.findAll());
            return "admin/student-registration";
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Passwords do not match");
            model.addAttribute("groups", groupService.findAll());
            return "admin/student-registration";
        }

        try {
            registrationService.registerStudent(registrationDto);
        } catch (UsernameAlreadyExistsException | ParseException e) {
            result.rejectValue("username", null, "Username already exists");
            model.addAttribute("groups", groupService.findAll());
            return "admin/student-registration";
        }

        return "home";
    }

    @GetMapping("/register/professorForm")

    public String showProfessorRegistrationForm(Model model) {
        model.addAttribute("professor", new ProfessorRegistrationDTO());
        model.addAttribute("departments", departmentService.findAll());
        return "admin/professor-registration";
    }

    @PostMapping("/register/professor")
    public String registerProfessor(@ModelAttribute("professor") @Valid ProfessorRegistrationDTO registrationDto,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "admin/professor-registration";
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Passwords do not match");
            model.addAttribute("departments", departmentService.findAll());
            return "admin/professor-registration";
        }

        try {
            registrationService.registerProfessor(registrationDto);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("username", null, "Username already exists");
            model.addAttribute("departments", departmentService.findAll());
            return "admin/professor-registration";
        }

        return "redirect:/admin/register/professor?success";
    }

    @GetMapping("/students/groups")
    public String getStudentsByGroup(Model model) {
        List<StudentGroup> groups = studentGroupService.findAll();
        model.addAttribute("groups", groups);
        return "admin/select-group";
    }
    @GetMapping("/students/group")
    public String getStudentsByGroupId(@RequestParam("groupId") int groupId, Model model) {
        List<Student> students = studentService.getStudentsByGroup(groupId);
        model.addAttribute("students", students);
        return "admin/students-list";
    }

    @GetMapping("/students/{id}/edit")
    public String editStudent(@PathVariable int id, Model model) {
        Student student = studentService.getStudentById(id);
        List<StudentGroup> groups = studentGroupService.findAll();
        model.addAttribute("groups", groups);
        model.addAttribute("student", student);
        return "admin/student-edit";
    }

    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute Student student) {
        studentService.saveOrUpdateStudent(student);
        return "redirect:/admin/students/groups";
    }

    @GetMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }


    @GetMapping("/professors/departments")
    public String getProfessorsByDepartment(Model model) {
        List<Department> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        return "admin/select-department";
    }
    @GetMapping("/professors/department")
    public String getProfessorsByDepartmentId(@RequestParam("departmentId") int departmentId, Model model) {
        List<Professor> professors = professorService.getProfessorsByDepartment(departmentId);
        model.addAttribute("professors", professors);
        return "admin/professors-list";
    }

    @GetMapping("/professors/{id}/edit")
    public String editProfessor(@PathVariable int id, Model model) {
        Professor professor = professorService.getProfessorById(id);
        List<Department> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("professor", professor);
        return "admin/professor-edit";
    }

    @PostMapping("/professors/save")
    public String saveProfessor(@ModelAttribute Professor professor) {
        professorService.saveOrUpdateProfessor(professor);
        return "redirect:/admin/professors/departments";
    }

    @GetMapping("/professors/{id}/delete")
    public String deleteProfessor(@PathVariable int id) {
        professorService.deleteProfessor(id);
        return "redirect:/admin/professors";
    }

}

