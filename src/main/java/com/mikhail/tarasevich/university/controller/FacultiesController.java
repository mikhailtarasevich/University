package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.FacultyRequest;
import com.mikhail.tarasevich.university.dto.FacultyResponse;
import com.mikhail.tarasevich.university.service.FacultyService;
import com.mikhail.tarasevich.university.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/faculties")
public class FacultiesController {

    private final FacultyService facultyService;
    private final GroupService groupService;

    @Autowired
    public FacultiesController(FacultyService facultyService, GroupService groupService) {
        this.facultyService = facultyService;
        this.groupService = groupService;
    }

    @GetMapping
    public String showAllFaculties(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("faculties", facultyService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", facultyService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "faculties/all-faculties";
    }

    @GetMapping("/{id}")
    public String showFaculty(@PathVariable("id") int id, Model model) {
        model.addAttribute("faculty", facultyService.findById(id));
        model.addAttribute("groups", groupService.findGroupsRelateToFaculty(id));

        return "faculties/show-faculty";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("faculty", new FacultyRequest());

        return "faculties/new";
    }

    @PostMapping
    public String register(@ModelAttribute("faculty") FacultyRequest facultyRequest) {
        facultyService.register(facultyRequest);

        return "redirect:/faculties";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(value = "id") int id, Model model) {
        FacultyResponse facultyResponse = facultyService.findById(id);
        FacultyRequest facultyRequest = new FacultyRequest();
        facultyRequest.setId(id);
        facultyRequest.setName(facultyResponse.getName());
        facultyRequest.setDescription(facultyResponse.getDescription());

        model.addAttribute("faculty", facultyRequest);

        return "faculties/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable(value = "id") int id,
                         @ModelAttribute(value = "faculty") FacultyRequest facultyRequest,
                         Model model) {
        facultyService.edit(facultyRequest);

        model.addAttribute("faculty", facultyService.findById(id));

        return "faculties/show-faculty";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") int id) {
        facultyService.deleteById(id);

        return "redirect:/faculties";
    }

}
