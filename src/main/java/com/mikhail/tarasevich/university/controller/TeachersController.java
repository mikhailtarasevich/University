package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.*;
import com.mikhail.tarasevich.university.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("teachers")
public class TeachersController {

    private static final String TEACHER = "teacher";
    private static final String GROUPS = "groups";
    private static final String COURSES = "courses";
    private static final String SHOW_TEACHER_PAGE = "teachers/show-teacher";

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
    public String showAllTeachers(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("teachers", teacherService.findAll(String.valueOf(page)));
        model.addAttribute("lastPage", teacherService.lastPageNumber());
        model.addAttribute("currentPage", page);

        return "teachers/all-teachers";
    }

    @GetMapping("/{id}")
    public String showTeacher(@PathVariable(value = "id") int id, Model model) {
        model.addAttribute(TEACHER, teacherService.findById(id));
        model.addAttribute(GROUPS, groupService.findGroupsRelateToTeacher(id));
        model.addAttribute(COURSES, courseService.findCoursesRelateToTeacher(id));

        return SHOW_TEACHER_PAGE;
    }

    @GetMapping("/new")
    public String createTeacher(Model model) {
        model.addAttribute(TEACHER, new TeacherRequest());
        model.addAttribute("teacherTitles", teacherTitleService.findAll());
        model.addAttribute("departments", departmentService.findAll());

        return "teachers/new";
    }

    @PostMapping
    public String register(@ModelAttribute(TEACHER) TeacherRequest teacherRequest) {
        teacherService.register(teacherRequest);

        return "redirect:/teachers";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(value = "id") int id, Model model) {
        TeacherResponse teacherResponse = teacherService.findById(id);

        TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setId(id);
        teacherRequest.setFirstName(teacherResponse.getFirstName());
        teacherRequest.setLastName(teacherResponse.getLastName());
        teacherRequest.setGender(teacherResponse.getGender());
        teacherRequest.setEmail(teacherResponse.getEmail());
        teacherRequest.setTeacherTitleId(teacherResponse.getTeacherTitle().getId());
        teacherRequest.setDepartmentId(teacherResponse.getDepartment().getId());

        model.addAttribute(TEACHER, teacherRequest);
        model.addAttribute("groupsRelateToTeacher", groupService.findGroupsRelateToTeacher(id));
        model.addAttribute("groupsNotRelateToTeacher", groupService.findGroupsNotRelateToTeacher(id));
        model.addAttribute("coursesRelateToTeacher", courseService.findCoursesRelateToTeacher(id));
        model.addAttribute("coursesRelateToDepartmentNotRelateToTeacher", courseService.findCoursesRelateToDepartmentNotRelateToTeacher(teacherResponse.getDepartment().getId(), id));
        model.addAttribute("teacherTitles", teacherTitleService.findAll());
        model.addAttribute("departments", departmentService.findAll());

        return "teachers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable(value = "id") int id,
                         @ModelAttribute(value = TEACHER) TeacherRequest teacherRequest,
                         Model model) {
        teacherService.editGeneralUserInfo(teacherRequest);

        model.addAttribute(TEACHER, teacherService.findById(id));
        model.addAttribute(GROUPS, groupService.findGroupsRelateToTeacher(id));
        model.addAttribute(COURSES, courseService.findCoursesRelateToTeacher(id));

        return SHOW_TEACHER_PAGE;
    }

    @PatchMapping("/{id}/edit/password")
    public String updatePassword(@PathVariable(value = "id") int id,
                                 @ModelAttribute(TEACHER) TeacherRequest teacherRequest,
                                 Model model) {
        teacherService.editPassword(teacherRequest);

        model.addAttribute(TEACHER, teacherService.findById(id));
        model.addAttribute(GROUPS, groupService.findGroupsRelateToTeacher(id));
        model.addAttribute(COURSES, courseService.findCoursesRelateToTeacher(id));

        return SHOW_TEACHER_PAGE;
    }

    @PatchMapping("/{id}/edit/teacher-title")
    public String updateTeacherTitle(@PathVariable(value = "id") int id,
                                     @ModelAttribute("teacherTitleId") int teacherTitleId,
                                     Model model) {
        teacherService.changeTeacherTeacherTitle(id, teacherTitleId);

        model.addAttribute(TEACHER, teacherService.findById(id));
        model.addAttribute(GROUPS, groupService.findGroupsRelateToTeacher(id));
        model.addAttribute(COURSES, courseService.findCoursesRelateToTeacher(id));

        return SHOW_TEACHER_PAGE;
    }

    @PatchMapping("/{id}/edit/department")
    public String updateDepartment(@PathVariable(value = "id") int id,
                                   @ModelAttribute("departmentId") int departmentId,
                                   Model model) {
        teacherService.changeTeacherDepartment(id, departmentId);

        model.addAttribute(TEACHER, teacherService.findById(id));
        model.addAttribute(GROUPS, groupService.findGroupsRelateToTeacher(id));
        model.addAttribute(COURSES, courseService.findCoursesRelateToTeacher(id));

        return SHOW_TEACHER_PAGE;
    }

    @PatchMapping("/{id}/subscribe/groups")
    public String subscribeTeacherToGroups(@PathVariable(value = "id") int id,
                                           @ModelAttribute(TEACHER) TeacherRequest teacherRequest,
                                           Model model) {
        teacherService.subscribeTeacherToGroups(id, teacherRequest.getGroupIds());

        model.addAttribute(TEACHER, teacherService.findById(id));
        model.addAttribute(GROUPS, groupService.findGroupsRelateToTeacher(id));
        model.addAttribute(COURSES, courseService.findCoursesRelateToTeacher(id));

        return SHOW_TEACHER_PAGE;
    }

    @PatchMapping("/{id}/unsubscribe/groups")
    public String unsubscribeTeacherFromGroups(@PathVariable(value = "id") int id,
                                               @ModelAttribute(TEACHER) TeacherRequest teacherRequest,
                                               Model model) {
        teacherService.unsubscribeTeacherFromGroups(id, teacherRequest.getGroupIds());

        model.addAttribute(TEACHER, teacherService.findById(id));
        model.addAttribute(GROUPS, groupService.findGroupsRelateToTeacher(id));
        model.addAttribute(COURSES, courseService.findCoursesRelateToTeacher(id));

        return SHOW_TEACHER_PAGE;
    }

    @PatchMapping("/{id}/subscribe/courses")
    public String subscribeTeacherToCourses(@PathVariable(value = "id") int id,
                                            @ModelAttribute(TEACHER) TeacherRequest teacherRequest,
                                            Model model) {

        teacherService.subscribeTeacherToCourses(id, teacherRequest.getCourseIds());

        model.addAttribute(TEACHER, teacherService.findById(id));
        model.addAttribute(GROUPS, groupService.findGroupsRelateToTeacher(id));
        model.addAttribute(COURSES, courseService.findCoursesRelateToTeacher(id));

        return SHOW_TEACHER_PAGE;
    }

    @PatchMapping("/{id}/unsubscribe/courses")
    public String unsubscribeTeacherToGroup(@PathVariable(value = "id") int id,
                                            @ModelAttribute(TEACHER) TeacherRequest teacherRequest,
                                            Model model) {
        teacherService.unsubscribeTeacherFromCourses(id, teacherRequest.getCourseIds());

        model.addAttribute(TEACHER, teacherService.findById(id));
        model.addAttribute(GROUPS, groupService.findGroupsRelateToTeacher(id));
        model.addAttribute(COURSES, courseService.findCoursesRelateToTeacher(id));

        return SHOW_TEACHER_PAGE;
    }

    @DeleteMapping("/{id}")
    public String deleteTeacher(@PathVariable(value = "id") int id) {
        teacherService.deleteById(id);

        return "redirect:/teachers";
    }

}
