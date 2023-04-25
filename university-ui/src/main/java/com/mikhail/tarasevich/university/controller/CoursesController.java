package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.dto.CourseResponse;
import com.mikhail.tarasevich.university.service.CourseService;
import com.mikhail.tarasevich.university.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/courses")
public class CoursesController {

    private final CourseService courseService;
    private final TeacherService teacherService;

    public CoursesController(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String showAllCourses(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("courses", courseService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", courseService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "courses/all-courses";
    }

    @GetMapping("/{id}")
    public String showCourse(@PathVariable("id") int id, Model model) {
        model.addAttribute("course", courseService.findById(id));
        model.addAttribute("teachers", teacherService.findTeachersRelateToCourse(id));

        return "courses/show-course";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("course", new CourseRequest());

        return "courses/new";
    }

    @PostMapping
    public String register(@ModelAttribute("course") CourseRequest courseRequest) {
        courseService.register(courseRequest);

        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(value = "id") int id, Model model) {
        CourseResponse courseResponse = courseService.findById(id);
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(id);
        courseRequest.setName(courseResponse.getName());
        courseRequest.setDescription(courseResponse.getDescription());

        model.addAttribute("course", courseRequest);

        return "courses/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable(value = "id") int id,
                         @ModelAttribute(value = "course") CourseRequest courseRequest,
                         Model model) {
        courseService.edit(courseRequest);

        model.addAttribute("course", courseService.findById(id));

        return "courses/show-course";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") int id) {
        courseService.deleteById(id);

        return "redirect:/courses";
    }

}
