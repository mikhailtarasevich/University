package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.dto.LessonTypeResponse;
import com.mikhail.tarasevich.university.service.LessonTypeService;
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

import java.time.Duration;

@Controller
@RequestMapping("/lesson-types")
public class LessonTypesController {

    private final LessonTypeService lessonTypeService;

    public LessonTypesController(LessonTypeService lessonTypeService) {
        this.lessonTypeService = lessonTypeService;
    }

    @GetMapping
    public String showAllLessonTypes(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("lessonTypes", lessonTypeService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", lessonTypeService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "lessonTypes/all-lesson-types";
    }

    @GetMapping("/{id}")
    public String showLessonType(@PathVariable("id") int id, Model model) {
        model.addAttribute("lessonType", lessonTypeService.findById(id));

        return "lessonTypes/show-lesson-types";
    }

    @GetMapping("/new")
    public String create() {
        return "lessonTypes/new";
    }

    @PostMapping
    public String register(@ModelAttribute("name") String name, @ModelAttribute("duration") Long duration) {
        LessonTypeRequest lessonTypeRequest = new LessonTypeRequest();
        lessonTypeRequest.setName(name);
        lessonTypeRequest.setDuration(Duration.ofMinutes(duration));

        lessonTypeService.register(lessonTypeRequest);

        return "redirect:/lesson-types";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(value = "id") int id, Model model) {
        LessonTypeResponse lessonTypeResponse = lessonTypeService.findById(id);
        LessonTypeRequest lessonTypeRequest = new LessonTypeRequest();
        lessonTypeRequest.setId(id);
        lessonTypeRequest.setName(lessonTypeResponse.getName());
        lessonTypeRequest.setDuration(lessonTypeResponse.getDuration());

        model.addAttribute("lessonType", lessonTypeRequest);

        return "lessonTypes/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable(value = "id") int id,
                         @ModelAttribute("name") String name,
                         @ModelAttribute("duration") Long duration,
                         Model model) {
        LessonTypeRequest lessonTypeRequest = new LessonTypeRequest();
        lessonTypeRequest.setId(id);
        lessonTypeRequest.setName(name);
        lessonTypeRequest.setDuration(Duration.ofMinutes(duration));

        lessonTypeService.edit(lessonTypeRequest);

        model.addAttribute("lessonType", lessonTypeService.findById(id));

        return "lessonTypes/show-lesson-types";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") int id) {
        lessonTypeService.deleteById(id);

        return "redirect:/lesson-types";
    }

}
