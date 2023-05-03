package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.dto.TeacherTitleResponse;
import com.mikhail.tarasevich.university.service.TeacherService;
import com.mikhail.tarasevich.university.service.TeacherTitleService;
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
@RequestMapping("/teacher-titles")
public class TeacherTitlesController {

    private final TeacherTitleService teacherTitleService;
    private final TeacherService teacherService;

    public TeacherTitlesController(TeacherTitleService teacherTitleService, TeacherService teacherService) {
        this.teacherTitleService = teacherTitleService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String showAllTeacherTitles(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("teacherTitles", teacherTitleService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", teacherTitleService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "teacherTitles/all-teacher-titles";
    }

    @GetMapping("/{id}")
    public String showTeacherTitle(@PathVariable("id") int id, Model model) {
        model.addAttribute("teacherTitle", teacherTitleService.findById(id));
        model.addAttribute("teachers", teacherService.findTeachersRelateToTeacherTitle(id));

        return "teacherTitles/show-teacher-title";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("teacherTitle", new TeacherTitleRequest());

        return "teacherTitles/new";
    }

    @PostMapping
    public String register(@ModelAttribute("teacherTitle") TeacherTitleRequest teacherTitleRequest) {
        teacherTitleService.register(teacherTitleRequest);

        return "redirect:/teacher-titles";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(value = "id") int id, Model model) {
        TeacherTitleResponse teacherTitleResponse = teacherTitleService.findById(id);
        TeacherTitleRequest teacherTitleRequest = new TeacherTitleRequest();
        teacherTitleRequest.setId(id);
        teacherTitleRequest.setName(teacherTitleResponse.getName());

        model.addAttribute("teacherTitle", teacherTitleRequest);

        return "teacherTitles/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable(value = "id") int id,
                         @ModelAttribute(value = "teacherTitle") TeacherTitleRequest teacherTitleRequest,
                         Model model) {
        teacherTitleService.edit(teacherTitleRequest);

        model.addAttribute("teacherTitle", teacherTitleService.findById(id));
        model.addAttribute("teachers", teacherService.findTeachersRelateToTeacherTitle(id));

        return "teacherTitles/show-teacher-title";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") int id) {
        teacherTitleService.deleteById(id);

        return "redirect:/teacher-titles";
    }

}
