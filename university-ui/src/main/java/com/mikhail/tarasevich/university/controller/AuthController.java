package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
public class AuthController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final TeacherTitleService teacherTitleService;
    private final DepartmentService departmentService;

    @Autowired
    public AuthController(StudentService studentService, TeacherService teacherService, GroupService groupService,
                          TeacherTitleService teacherTitleService, DepartmentService departmentService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.teacherTitleService = teacherTitleService;
        this.departmentService = departmentService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "auth/registration";
    }

    @GetMapping("/new/student")
    public String newStudent(Model model) {
        model.addAttribute("student", new StudentRequest());
        model.addAttribute("groups", groupService.findAll());

        return "auth/student-register";
    }

    @GetMapping("/new/teacher")
    public String newTeacher(Model model) {
        model.addAttribute("teacher", new TeacherRequest());
        model.addAttribute("teacherTitles", teacherTitleService.findAll());
        model.addAttribute("departments", departmentService.findAll());

        return "auth/teacher-register";
    }

    @PostMapping("/teacher")
    public String registerTeacher(@ModelAttribute("teacher") TeacherRequest teacherRequest) {
        teacherService.register(teacherRequest);

        return "redirect:/auth/login";
    }

    @PostMapping("/student")
    public String registerStudent(@ModelAttribute("student") StudentRequest studentRequest) {
        studentService.register(studentRequest);

        return "redirect:/auth/login";
    }

}
