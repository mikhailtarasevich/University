package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.dto.LessonResponse;
import com.mikhail.tarasevich.university.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("lessons")
public class LessonsController {

    private final LessonService lessonService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final LessonTypeService lessonTypeService;

    @Autowired
    public LessonsController(LessonService lessonService, GroupService groupService, TeacherService teacherService,
                             CourseService courseService, LessonTypeService lessonTypeService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.lessonTypeService = lessonTypeService;
    }

    @GetMapping
    public String showAllLessons(@RequestParam(value = "page", defaultValue = "1") int page,
                                 Model model) {
        model.addAttribute("lessons", lessonService.findAll(String.valueOf(page)));
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("lastPage", lessonService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "lessons/all-lessons";
    }

    @GetMapping("/{id}")
    public String showLesson(@PathVariable("id") int id, Model model) {
        model.addAttribute("lesson", lessonService.findById(id));

        return "lessons/show-lesson";
    }

    @GetMapping("/group")
    public String showAllLessonsRelateToGroup(@ModelAttribute(value = "groupId") int id,
                                              Model model) {
        model.addAttribute("lessons", lessonService.findLessonsRelateToGroup(id));
        model.addAttribute("groups", groupService.findAll());

        return "lessons/all-lessons-group";
    }

    @GetMapping("/new")
    public String newLesson(Model model) {
        model.addAttribute("lesson", new LessonRequest());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("lessonTypes", lessonTypeService.findAll());

        return "lessons/new";
    }

    @PostMapping
    public String register(@ModelAttribute("lesson") LessonRequest lessonRequest,
                           @ModelAttribute("dateTime") String dateTime) {
        lessonRequest.setStartTime(dateTime.isEmpty() ? null : LocalDateTime.parse(dateTime));
        lessonService.register(lessonRequest);

        return "redirect:/lessons";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        LessonResponse lessonResponse = lessonService.findById(id);

        LessonRequest lessonForUpdate = new LessonRequest();
        lessonForUpdate.setId(id);
        lessonForUpdate.setName(lessonResponse.getName());
        lessonForUpdate.setGroupId(lessonResponse.getGroup().getId());
        lessonForUpdate.setTeacherId(lessonResponse.getTeacher().getId());
        lessonForUpdate.setCourseId(lessonResponse.getCourse().getId());
        lessonForUpdate.setLessonTypeId(lessonResponse.getLessonType().getId());
        lessonForUpdate.setStartTime(lessonResponse.getStartTime());

        model.addAttribute("lesson", lessonForUpdate);
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("lessonTypes", lessonTypeService.findAll());

        return "lessons/edit";
    }

    @PatchMapping("/{id}")
    public String updateLesson(@ModelAttribute("lesson") LessonRequest lessonRequest,
                               @ModelAttribute("dateTime") String dateTime,
                               Model model) {
        lessonRequest.setStartTime(LocalDateTime.parse(dateTime));
        lessonService.edit(lessonRequest);

        model.addAttribute("lesson", lessonService.findById(lessonRequest.getId()));

        return "lessons/show-lesson";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        lessonService.deleteById(id);

        return "redirect:/lessons";
    }

}
