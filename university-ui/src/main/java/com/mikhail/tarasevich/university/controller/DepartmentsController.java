package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.dto.DepartmentResponse;
import com.mikhail.tarasevich.university.service.CourseService;
import com.mikhail.tarasevich.university.service.DepartmentService;
import com.mikhail.tarasevich.university.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("departments")
public class DepartmentsController {

    private final DepartmentService departmentService;
    private final TeacherService teacherService;
    private final CourseService courseService;

    public DepartmentsController(DepartmentService departmentService, TeacherService teacherService,
                                 CourseService courseService) {
        this.departmentService = departmentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @GetMapping
    public String showAllDepartments(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("departments", departmentService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", departmentService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "departments/all-departments";
    }

    @GetMapping("/{id}")
    public String showDepartment(@PathVariable(value = "id") int id, Model model) {
        model.addAttribute("department", departmentService.findById(id));
        model.addAttribute("teachers", teacherService.findTeachersRelateToDepartment(id));
        model.addAttribute("courses", courseService.findCoursesRelateToDepartment(id));

        return "departments/show-department";
    }

    @GetMapping("/new")
    public String createDepartment(Model model) {
        model.addAttribute("department", new DepartmentRequest());
        model.addAttribute("courses", courseService.findAll());

        return "departments/new";
    }

    @PostMapping
    public String register(@ModelAttribute("department") DepartmentRequest departmentRequest) {
        DepartmentResponse saved = departmentService.register(departmentRequest);
        departmentService.addCoursesToDepartment(saved.getId(), departmentRequest.getCourseIds());

        return "redirect:/departments";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(value = "id") int id, Model model) {
        DepartmentResponse departmentResponse = departmentService.findById(id);

        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(id);
        departmentRequest.setName(departmentResponse.getName());
        departmentRequest.setDescription(departmentResponse.getDescription());

        model.addAttribute("department", departmentRequest);
        model.addAttribute("coursesRelateToDepartment", courseService.findCoursesRelateToDepartment(id));
        model.addAttribute("coursesNotRelateToDepartment",
                courseService.findCoursesNotRelateToDepartment(id));

        return "departments/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable(value = "id") int id,
                         @ModelAttribute(value = "department") DepartmentRequest departmentRequest,
                         Model model) {
        departmentService.edit(departmentRequest);

        model.addAttribute("department", departmentService.findById(id));
        model.addAttribute("teachers", teacherService.findTeachersRelateToDepartment(id));
        model.addAttribute("courses", courseService.findCoursesRelateToDepartment(id));

        return "departments/show-department";
    }

    @PatchMapping("/{id}/add/courses")
    public String addCoursesToDepartment(@PathVariable(value = "id") int id,
                                         @ModelAttribute("department") DepartmentRequest departmentRequest,
                                         Model model) {
        departmentService.addCoursesToDepartment(id, departmentRequest.getCourseIds());

        model.addAttribute("department", departmentService.findById(id));
        model.addAttribute("teachers", teacherService.findTeachersRelateToDepartment(id));
        model.addAttribute("courses", courseService.findCoursesRelateToDepartment(id));

        return "departments/show-department";
    }

    @PatchMapping("/{id}/delete/courses")
    public String deleteCoursesFromDepartment(@PathVariable(value = "id") int id,
                                              @ModelAttribute("department") DepartmentRequest departmentRequest,
                                              Model model) {
        departmentService.deleteCoursesFromDepartment(id, departmentRequest.getCourseIds());

        model.addAttribute("department", departmentService.findById(id));
        model.addAttribute("teachers", teacherService.findTeachersRelateToDepartment(id));
        model.addAttribute("courses", courseService.findCoursesRelateToDepartment(id));

        return "departments/show-department";
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable(value = "id") int id) {
        departmentService.deleteById(id);

        return "redirect:/departments";
    }

}
