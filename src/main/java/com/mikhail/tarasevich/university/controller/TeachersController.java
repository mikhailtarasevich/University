package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("teachers")
public class TeachersController {

    private final TeacherService teacherService;

    @Autowired
    public TeachersController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public String showAllTeachers (@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("teachers", teacherService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", teacherService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "teachers/all-teachers";
    }

    @GetMapping("/{id}")
    public String showTeacher (@PathVariable(value = "id") int id, Model model) {
        model.addAttribute("teacher", teacherService.findById(id));

        return "teachers/show-teacher";
    }

}
