package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.dto.GroupResponse;
import com.mikhail.tarasevich.university.service.EducationFormService;
import com.mikhail.tarasevich.university.service.FacultyService;
import com.mikhail.tarasevich.university.service.GroupService;
import com.mikhail.tarasevich.university.service.StudentService;
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
@RequestMapping("groups")
public class GroupsController {

    private final GroupService groupService;
    private final FacultyService facultyService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final EducationFormService educationFormService;

    public GroupsController(GroupService groupService, FacultyService facultyService, StudentService studentService,
                            TeacherService teacherService, EducationFormService educationFormService) {
        this.groupService = groupService;
        this.facultyService = facultyService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.educationFormService = educationFormService;
    }

    @GetMapping
    public String showAllGroups(@RequestParam(value = "page", defaultValue = "1") int page,
                                Model model) {
        model.addAttribute("groups", groupService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", groupService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "groups/all-groups";
    }

    @GetMapping("/{id}")
    public String showGroup(@PathVariable("id") int id, Model model) {
        model.addAttribute("group", groupService.findById(id));
        model.addAttribute("students", studentService.findStudentsRelateToGroup(id));
        model.addAttribute("teachers", teacherService.findTeachersRelateToGroup(id));

        return "groups/show-group";
    }

    @GetMapping("/new")
    public String newGroup(Model model) {
        model.addAttribute("group", new GroupRequest());
        model.addAttribute("faculties", facultyService.findAll());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("educationForms", educationFormService.findAll());

        return "groups/new";
    }

    @PostMapping
    public String register(@ModelAttribute("group") GroupRequest groupRequest) {
        groupService.register(groupRequest);

        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        GroupResponse groupResponse = groupService.findById(id);

        GroupRequest groupForUpdate = new GroupRequest();
        groupForUpdate.setId(id);
        groupForUpdate.setName(groupResponse.getName());
        groupForUpdate.setFacultyId(groupResponse.getFaculty() == null ? 0 : groupResponse.getFaculty().getId());
        groupForUpdate.setHeadStudentId(groupResponse.getHeadStudent() == null ? 0 : groupResponse.getHeadStudent().getId());
        groupForUpdate.setEducationFormId(groupResponse.getEducationForm() == null ? 0 : groupResponse.getEducationForm().getId());

        model.addAttribute("group", groupForUpdate);
        model.addAttribute("faculties", facultyService.findAll());
        model.addAttribute("students", studentService.findStudentsRelateToGroup(id));
        model.addAttribute("educationForms", educationFormService.findAll());

        return "groups/edit";
    }

    @PatchMapping("/{id}")
    public String updateGroup(@PathVariable("id") int id,
                              @ModelAttribute("group") GroupRequest groupRequest,
                              Model model) {
        groupService.edit(groupRequest);

        model.addAttribute("group", groupService.findById(id));
        model.addAttribute("students", studentService.findStudentsRelateToGroup(id));
        model.addAttribute("teachers", teacherService.findTeachersRelateToGroup(id));

        return "groups/show-group";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        groupService.deleteById(id);

        return "redirect:/groups";
    }

}
