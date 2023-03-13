package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("teachers")
public class TeachersController {

    private final TeacherService teacherService;
    private final TeacherTitleService teacherTitleService;
    private final DepartmentService departmentService;
    private final GroupService groupService;
    private final CourseService courseService;

    @Autowired
    public TeachersController(TeacherService teacherService, TeacherTitleService teacherTitleService,
                              DepartmentService departmentService, GroupService groupService,
                              CourseService courseService) {
        this.teacherService = teacherService;
        this.teacherTitleService = teacherTitleService;
        this.departmentService = departmentService;
        this.groupService = groupService;
        this.courseService = courseService;
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
        model.addAttribute("groups", groupService.findGroupsRelateToTeacher(id));
        model.addAttribute("courses", courseService.findCoursesRelateToTeacher(id));

        return "teachers/show-teacher";
    }

    @GetMapping("/new")
    public String createTeacher (Model model) {
        model.addAttribute("teacher", new TeacherRequest());
        model.addAttribute("teacherTitles", teacherTitleService.findAll());
        model.addAttribute("departments", departmentService.findAll());

        return "teachers/new";
    }

    @PostMapping
    public String register (@ModelAttribute("teacher") TeacherRequest teacherRequest) {
        teacherService.register(teacherRequest);

        return "redirect:/teachers";
    }

}
