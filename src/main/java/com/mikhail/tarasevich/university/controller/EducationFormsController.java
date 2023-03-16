package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.EducationFormRequest;
import com.mikhail.tarasevich.university.dto.EducationFormResponse;
import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.service.EducationFormService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/education-forms")
public class EducationFormsController {

    private final EducationFormService educationFormService;

    public EducationFormsController(EducationFormService educationFormService) {
        this.educationFormService = educationFormService;
    }

    @GetMapping
    public String showAllEducationForms(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("educationForms", educationFormService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", educationFormService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "educationForms/all-education-forms";
    }

    @GetMapping("/{id}")
    public String showEducationForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("educationForm", educationFormService.findById(id));

        return "educationForms/show-education-form";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("educationForm", new TeacherTitleRequest());

        return "educationForms/new";
    }

    @PostMapping
    public String register(@ModelAttribute("educationForm") EducationFormRequest educationFormRequest) {
        educationFormService.register(educationFormRequest);

        return "redirect:/education-forms";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(value = "id") int id, Model model) {
        EducationFormResponse educationFormResponse = educationFormService.findById(id);
        EducationFormRequest educationFormRequest = new EducationFormRequest();
        educationFormRequest.setId(id);
        educationFormRequest.setName(educationFormResponse.getName());

        model.addAttribute("educationForm", educationFormRequest);

        return "educationForms/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable(value = "id") int id,
                         @ModelAttribute(value = "educationForm") EducationFormRequest educationFormRequest,
                         Model model) {
        educationFormService.edit(educationFormRequest);

        model.addAttribute("educationForm", educationFormService.findById(id));

        return "educationForms/show-education-form";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") int id) {
        educationFormService.deleteById(id);

        return "redirect:/education-forms";
    }

}
