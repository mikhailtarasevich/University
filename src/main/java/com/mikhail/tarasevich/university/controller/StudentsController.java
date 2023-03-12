package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.service.GroupService;
import com.mikhail.tarasevich.university.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("students")
public class StudentsController {

    private final StudentService studentService;
    private final GroupService groupService;

    @Autowired
    public StudentsController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String showAllStudents(@RequestParam(value = "page", defaultValue = "1") int page,
                                  Model model) {
        model.addAttribute("students", studentService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", studentService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "students/all-students";
    }

    @GetMapping("/{id}")
    public String showStudent(@PathVariable("id") int id, Model model) {
        model.addAttribute("student", studentService.findById(id));

        return "students/show-student";
    }

    @GetMapping("/new")
    public String newStudent(Model model) {
        model.addAttribute("student", new StudentRequest());
        model.addAttribute("groups", groupService.findAll());

        return "students/new";
    }

    @PostMapping()
    public String register(@ModelAttribute("student") StudentRequest studentRequest) {
        studentService.register(studentRequest);

        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        StudentResponse studentResponse = studentService.findById(id);

        StudentRequest studentForUpdate = new StudentRequest();
        studentForUpdate.setId(id);
        studentForUpdate.setFirstName(studentResponse.getFirstName());
        studentForUpdate.setLastName(studentResponse.getLastName());
        studentForUpdate.setGender(studentResponse.getGender());
        studentForUpdate.setEmail(studentResponse.getEmail());
        studentForUpdate.setGroupId(studentResponse.getGroup().getId());

        model.addAttribute("student", studentForUpdate);
        model.addAttribute("groups", groupService.findAll());

        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") StudentRequest studentRequest, Model model) {
        studentRequest.setGroupId(0);
        studentService.editGeneralUserInfo(studentRequest);
        model.addAttribute("student", studentService.findById(studentRequest.getId()));

        return "students/show-student";
    }

    @PatchMapping("/{id}/edit-password")
    public String updatePassword(@ModelAttribute("student") StudentRequest studentRequest, Model model) {
        studentService.editPassword(studentRequest);
        model.addAttribute("student", studentService.findById(studentRequest.getId()));

        return "students/show-student";
    }

    @PatchMapping("/{id}/edit-group")
    public String updateGroup(@ModelAttribute("student") StudentRequest studentRequest, Model model) {
        studentService.subscribeUserToGroup(studentRequest.getId(), studentRequest.getGroupId());
        model.addAttribute("student", studentService.findById(studentRequest.getId()));

        return "students/show-student";
    }

    @PatchMapping("/{id}/leave-group")
    public String leaveGroup(@ModelAttribute("student") StudentRequest studentRequest, Model model) {
        studentService.unsubscribeStudentFromGroup(studentRequest.getId());
        model.addAttribute("student", studentService.findById(studentRequest.getId()));

        return "students/show-student";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        studentService.deleteById(id);

        return "redirect:/students";
    }

}
