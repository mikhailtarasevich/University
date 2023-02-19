package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.*;
import com.mikhail.tarasevich.university.entity.*;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.provider.ViewProvider;
import com.mikhail.tarasevich.university.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FrontController {
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final GroupService groupService;
    private final DepartmentService departmentService;
    private final LessonService lessonService;
    private final TeacherTitleService teacherTitleService;
    private final LessonTypeService lessonTypeService;
    private final FacultyService facultyService;
    private final EducationFormService educationFormService;
    private final ViewProvider viewProvider;

    @Autowired
    public FrontController(CourseService courseService, TeacherService teacherService,
                           StudentService studentService, GroupService groupService,
                           DepartmentService departmentService, LessonService lessonService,
                           TeacherTitleService teacherTitleService, LessonTypeService lessonTypeService,
                           FacultyService facultyService, EducationFormService educationFormService,
                           ViewProvider viewProvider) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.groupService = groupService;
        this.departmentService = departmentService;
        this.lessonService = lessonService;
        this.teacherTitleService = teacherTitleService;
        this.lessonTypeService = lessonTypeService;
        this.facultyService = facultyService;
        this.educationFormService = educationFormService;
        this.viewProvider = viewProvider;
    }

    public void startMenu() {

        int chooser;

        viewProvider.printMessage("\nPress 0 to exit;\n" +
                "Press 1 to do actions with teacher's entities;\n" +
                "Press 2 to do actions with student's entities;\n" +
                "Press 3 to do actions with course's entities;\n" +
                "Press 4 to do actions with group's entities;\n" +
                "Press 5 to do actions with department's entities;\n" +
                "Press 6 to do actions with lesson's entities;\n" +
                "Press 7 to do actions with teacher titles entities;\n" +
                "Press 8 to do actions with lesson types entities;\n" +
                "Press 9 to do actions with faculty's entities;\n" +
                "Press 10 to do actions with education forms entities;\n");

        chooser = viewProvider.readInt();

        switch (chooser) {

            case 0:
                viewProvider.printMessage("Goodbye");
                break;

            case 1:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find all teachers;\n" +
                        "Press 2 to find a teacher by id;\n" +
                        "Press 3 to add new teacher;\n" +
                        "Press 4 to delete a teacher;\n" +
                        "Press 5 to add a teacher to a group;\n" +
                        "Press 6 to delete a teacher from a group;\n" +
                        "Press 7 to add a teacher to a course;\n" +
                        "Press 8 to delete a teacher from a course;\n" +
                        "Press 9 to find all teacher relate to groups;\n" +
                        "Press 10 to find all teacher relate to courses;\n" +
                        "Press 11 to find all teacher relate to department;\n");

                chooser = viewProvider.readInt();
                switch (chooser) {
                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findTeachers();
                        returnToMenu();
                        break;

                    case 2:
                        findTeacherById();
                        returnToMenu();
                        break;

                    case 3:
                        saveTeacher();
                        returnToMenu();
                        break;

                    case 4:
                        deleteTeacher();
                        returnToMenu();
                        break;

                    case 5:
                        addTeacherToGroup();
                        returnToMenu();
                        break;

                    case 6:
                        deleteTeacherFromGroup();
                        returnToMenu();
                        break;

                    case 7:
                        addTeacherToCourse();
                        returnToMenu();
                        break;

                    case 8:
                        deleteTeacherFromCourse();
                        returnToMenu();
                        break;

                    case 9:
                        findTeachersRelateToGroup();
                        returnToMenu();
                        break;

                    case 10:
                        findTeachersRelateToCourse();
                        returnToMenu();
                        break;

                    case 11:
                        findAllCoursesRelateToDepartment();
                        returnToMenu();
                        break;

                    default:
                        break;
                }
                break;

            case 2:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find all students;\n" +
                        "Press 2 to find a student by id;\n" +
                        "Press 3 to add new student;\n" +
                        "Press 4 to delete a student;\n" +
                        "Press 5 to delete student's from a group;\n" +
                        "Press 6 to find students relate to group;\n");

                chooser = viewProvider.readInt();
                switch (chooser) {

                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findStudents();
                        returnToMenu();
                        break;

                    case 2:
                        findStudentById();
                        returnToMenu();
                        break;

                    case 3:
                        saveStudent();
                        returnToMenu();
                        break;

                    case 4:
                        deleteStudent();
                        returnToMenu();
                        break;

                    case 5:
                        unsubscribeStudentFromGroup();
                        returnToMenu();
                        break;

                    case 6:
                        findStudentRelateToGroup();
                        returnToMenu();
                        break;

                    default:
                        break;

                }

                break;

            case 3:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find all courses;\n" +
                        "Press 2 to find a course by id;\n" +
                        "Press 3 to add new course;\n" +
                        "Press 4 to add new courses;\n" +
                        "Press 5 to update course;\n" +
                        "Press 6 to delete a course;\n" +
                        "Press 7 to delete a courses;\n" +
                        "Press 8 to find all courses relate to department;\n" +
                        "Press 9 to find all courses relate to teacher;\n");

                chooser = viewProvider.readInt();
                switch (chooser) {

                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findCourses();
                        returnToMenu();
                        break;

                    case 2:
                        findCourseById();
                        returnToMenu();
                        break;

                    case 3:
                        saveCourse();
                        returnToMenu();
                        break;

                    case 4:
                        saveCourses();
                        returnToMenu();
                        break;

                    case 5:
                        updateCourse();
                        returnToMenu();
                        break;

                    case 6:
                        deleteCourse();
                        returnToMenu();
                        break;

                    case 7:
                        deleteCourses();
                        returnToMenu();
                        break;

                    case 8:
                        findAllCoursesRelateToDepartment();
                        returnToMenu();
                        break;

                    case 9:
                        findAllCoursesRelateToTeacher();
                        returnToMenu();
                        break;


                    default:
                        break;
                }
                break;

            case 4:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find all groups;\n" +
                        "Press 2 to find a group by id;\n" +
                        "Press 3 to add new group;\n" +
                        "Press 4 to delete a group;\n" +
                        "Press 5 to find groups relate to teacher;\n");

                chooser = viewProvider.readInt();

                switch (chooser) {

                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findGroups();
                        returnToMenu();
                        break;

                    case 2:
                        findGroupById();
                        returnToMenu();
                        break;

                    case 3:
                        saveGroup();
                        returnToMenu();
                        break;

                    case 4:
                        deleteGroup();
                        returnToMenu();
                        break;

                    case 5:
                        findGroupsRelateToTeacher();
                        returnToMenu();
                        break;

                    default:
                        break;
                }

                break;

            case 5:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find all departments;\n" +
                        "Press 2 to find a department by id;\n" +
                        "Press 3 to add new department;\n" +
                        "Press 4 to delete a department;\n");

                chooser = viewProvider.readInt();

                switch (chooser) {

                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findDepartments();
                        returnToMenu();
                        break;

                    case 2:
                        findDepartmentById();
                        returnToMenu();
                        break;

                    case 3:
                        saveDepartment();
                        returnToMenu();
                        break;

                    case 4:
                        deleteDepartment();
                        returnToMenu();
                        break;

                    default:
                        break;
                }

                break;

            case 6:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find all lessons;\n" +
                        "Press 2 to find a lesson by id;\n" +
                        "Press 3 to add new lesson;\n" +
                        "Press 4 to delete a lesson;\n" +
                        "Press 5 to lessons relate to group;\n");

                chooser = viewProvider.readInt();

                switch (chooser) {

                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findLessons();
                        returnToMenu();
                        break;

                    case 2:
                        findLessonById();
                        returnToMenu();
                        break;

                    case 3:
                        saveLesson();
                        returnToMenu();
                        break;

                    case 4:
                        deleteLesson();
                        returnToMenu();
                        break;

                    case 5:
                        findLessonsRelateToGroup();
                        returnToMenu();
                        break;

                    default:
                        break;
                }

                break;

            case 7:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find all teacher titles;\n" +
                        "Press 2 to find a teacher title by id;\n" +
                        "Press 3 to add new teacher title;\n" +
                        "Press 4 to delete a teacher title;\n");

                chooser = viewProvider.readInt();

                switch (chooser) {

                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findTeacherTitles();
                        returnToMenu();
                        break;

                    case 2:
                        findTeacherTitleById();
                        returnToMenu();
                        break;

                    case 3:
                        saveTeacherTitle();
                        returnToMenu();
                        break;

                    case 4:
                        deleteTeacherTitle();
                        returnToMenu();
                        break;

                    default:
                        break;
                }

                break;

            case 8:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find all lesson types;\n" +
                        "Press 2 to find a lesson type by id;\n" +
                        "Press 3 to add new lesson type;\n" +
                        "Press 4 to delete a lesson type;\n");

                chooser = viewProvider.readInt();

                switch (chooser) {

                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findLessonTypes();
                        returnToMenu();
                        break;

                    case 2:
                        findLessonTypeById();
                        returnToMenu();
                        break;

                    case 3:
                        saveLessonType();
                        returnToMenu();
                        break;

                    case 4:
                        deleteLessonType();
                        returnToMenu();
                        break;

                    default:
                        break;
                }

                break;

            case 9:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find faculties;\n" +
                        "Press 2 to find a faculty by id;\n" +
                        "Press 3 to add new faculty;\n" +
                        "Press 4 to delete a faculty;\n");

                chooser = viewProvider.readInt();

                switch (chooser) {

                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findFaculties();
                        returnToMenu();
                        break;

                    case 2:
                        findFacultyById();
                        returnToMenu();
                        break;

                    case 3:
                        saveFaculty();
                        returnToMenu();
                        break;

                    case 4:
                        deleteFaculty();
                        returnToMenu();
                        break;

                    default:
                        break;
                }

                break;

            case 10:
                viewProvider.printMessage("\nPress 0 to exit to start menu;\n" +
                        "Press 1 to find education forms;\n" +
                        "Press 2 to find a education form by id;\n" +
                        "Press 3 to add new education form;\n" +
                        "Press 4 to delete a education form;\n");

                chooser = viewProvider.readInt();

                switch (chooser) {

                    case 0:
                        startMenu();
                        break;

                    case 1:
                        findEducationForms();
                        returnToMenu();
                        break;

                    case 2:
                        findEducationFormById();
                        returnToMenu();
                        break;

                    case 3:
                        saveEducationForm();
                        returnToMenu();
                        break;

                    case 4:
                        deleteEducationForm();
                        returnToMenu();
                        break;

                    default:
                        break;
                }

                break;

            default:
                startMenu();
        }
    }

    //Teachers

    private void findTeachers() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        String pageNumber = viewProvider.read();
        viewProvider.printMessage(teacherService.findAll(pageNumber).toString());
    }

    private void findTeacherById() {
        viewProvider.printMessage("\nPlease, type teacher's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(teacherService.findById(id).toString());
    }

    private void saveTeacher() {
        TeacherRequest teacher = makeNewTeacher();
        TeacherResponse savedTeacher = teacherService.register(teacher);
        viewProvider.printMessage("The teacher with these parameters has been added to database: \n" +
                savedTeacher);
    }

    private TeacherRequest makeNewTeacher() {

        TeacherRequest r = new TeacherRequest();
        viewProvider.printMessage("\nPlease, type first name:");
        r.setFirstName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type last name:");
        r.setLastName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type gender in digit representation(MALE = 0 /FEMALE = 1):\n");
        r.setGender(Gender.getById(viewProvider.readInt()));
        viewProvider.printMessage("\nPlease, type email:");
        r.setEmail(viewProvider.read());
        viewProvider.printMessage("\nPlease, type password:");
        r.setPassword(viewProvider.read());
        viewProvider.printMessage("\nPlease, type teacherTitleId:");
        r.setTeacherTitle(TeacherTitle.builder().withId(viewProvider.readInt()).build());
        viewProvider.printMessage("\nPlease, type departmentId:");
        r.setDepartment(Department.builder().withId(viewProvider.readInt()).build());

        return r;
    }

    private void deleteTeacher() {
        viewProvider.printMessage("Which teacher do you want to delete? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        teacherService.deleteById(teacherId);
        viewProvider.printMessage("The teacher with id = " + teacherId + " has been deleted from database\n");
    }

    private void addTeacherToGroup() {
        viewProvider.printMessage("Which teacher do you want to add to the group? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the group id: \n");
        int groupId = viewProvider.readInt();
        teacherService.subscribeUserToGroup(teacherId, groupId);
    }

    private void deleteTeacherFromGroup() {
        viewProvider.printMessage("Which teacher do you want to delete from the group? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the group id: \n");
        int groupId = viewProvider.readInt();
        teacherService.unsubscribeTeacherFromGroup(teacherId, groupId);
    }

    private void addTeacherToCourse() {
        viewProvider.printMessage("Which teacher do you want to add to the course? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the course id: \n");
        int courseId = viewProvider.readInt();
        teacherService.subscribeTeacherToCourse(teacherId, courseId);
    }

    private void deleteTeacherFromCourse() {
        viewProvider.printMessage("Which teacher do you want to delete from the course? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the course id: \n");
        int courseId = viewProvider.readInt();
        teacherService.unsubscribeTeacherFromCourse(teacherId, courseId);
    }

    private void findTeachersRelateToGroup() {
        viewProvider.printMessage("Please, type the group id which teachers you want to find: \n");
        viewProvider.printMessage(teacherService.findTeachersRelateToGroup(viewProvider.readInt()).toString());
    }

    private void findTeachersRelateToCourse() {
        viewProvider.printMessage("Please, type the course id which teachers you want to find: \n");
        viewProvider.printMessage(teacherService.findTeachersRelateToCourse(viewProvider.readInt()).toString());
    }

    private void findTeachersRelateToDepartment() {
        viewProvider.printMessage("Please, type the department id which teachers you want to find: \n");
        viewProvider.printMessage(teacherService.findTeachersRelateToDepartment(viewProvider.readInt()).toString());
    }

    //    Students

    private void findStudents() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        viewProvider.printMessage(studentService.findAll(viewProvider.read()).toString());
    }

    private void findStudentById() {
        viewProvider.printMessage("\nPlease, type student's id:");
        viewProvider.printMessage(studentService.findById(viewProvider.readInt()).toString());
    }

    private void saveStudent() {
        StudentRequest student = makeNewStudent();
        StudentResponse savedStudent = studentService.register(student);
        viewProvider.printMessage("The student with these parameters has been added to database: \n" +
                savedStudent);
    }

    private StudentRequest makeNewStudent() {
        StudentRequest s = new StudentRequest();
        viewProvider.printMessage("\nPlease, type first name:");
        s.setFirstName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type last name:");
        s.setLastName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type gender in digit representation(MALE = 0 /FEMALE = 1):\n");
        s.setGender(Gender.getById(viewProvider.readInt()));
        viewProvider.printMessage("\nPlease, type email:");
        s.setEmail(viewProvider.read());
        viewProvider.printMessage("\nPlease, type password:");
        s.setPassword(viewProvider.read());
        viewProvider.printMessage("\nPlease, type groupId:");
        s.setGroup(Group.builder().withId(viewProvider.readInt()).build());

        return s;
    }

    private void deleteStudent() {
        viewProvider.printMessage("Which student do you want to delete? Please, type the student id: \n");
        int studentId = viewProvider.readInt();
        studentService.deleteById(studentId);
        viewProvider.printMessage("The student with id = " + studentId + " has been deleted from database\n");
    }

    private void unsubscribeStudentFromGroup() {
        viewProvider.printMessage("Which student do you want to unsubscribe from the group? Please, type the student id: \n");
        int studentId = viewProvider.readInt();
        studentService.unsubscribeStudentFromGroup(studentId);
    }

    private void findStudentRelateToGroup() {
        viewProvider.printMessage("Students from which group do you want to find? Please, type the group id: \n");
        int groupId = viewProvider.readInt();
        studentService.findStudentsRelateToGroup(groupId);
    }

    //    Courses

    private void findCourses() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        String pageNumber = viewProvider.read();
        List<CourseResponse> courses = courseService.findAll(pageNumber);
        viewProvider.printMessage(courses.toString());
    }

    private void findCourseById() {
        viewProvider.printMessage("\nPlease, type course's id:");
        int id = viewProvider.readInt();
        try {
            viewProvider.printMessage(courseService.findById(id).toString());
        } catch (NumberFormatException e) {
            viewProvider.printMessage("There seems to be an error in what you have typed");
        }
    }

    private void saveCourse() {
        CourseRequest course = makeCourseForSave();

        try {
            CourseResponse savedCourse = courseService.register(course);
            viewProvider.printMessage("The course with these parameters has been added to database: " + savedCourse);
        } catch (IncorrectRequestData e) {
            viewProvider.printMessage(e.getMessage());
        }
    }

    private CourseRequest makeCourseForSave() {
        CourseRequest courseRequest = new CourseRequest();
        viewProvider.printMessage("\nPlease, type course name:");
        courseRequest.setName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type course's description:");
        courseRequest.setDescription(viewProvider.read());

        return courseRequest;
    }

    private void saveCourses() {
        List<CourseRequest> coursesForSave = new ArrayList<>();

        try {
            courseService.registerAll(makeCoursesForSave(coursesForSave));
            viewProvider.printMessage("The courses have been added to database.");
        } catch (IncorrectRequestData e) {
            viewProvider.printMessage(e.getMessage());
        }
    }

    private List<CourseRequest> makeCoursesForSave(List<CourseRequest> coursesForSave) {

        CourseRequest courseRequest = new CourseRequest();
        viewProvider.printMessage("\nPlease, type course name:");
        courseRequest.setName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type course's description:");
        courseRequest.setDescription(viewProvider.read());

        coursesForSave.add(courseRequest);

        viewProvider.printMessage("\nCourse was added to save list.");
        viewProvider.printMessage("\nDo you want to add one more course to save list? Type Y/N (yes/no): ");
        String addMore = viewProvider.read();
        if (addMore.equalsIgnoreCase("y")) makeCoursesForSave(coursesForSave);

        return coursesForSave;
    }

    private void updateCourse() {
        CourseRequest course = makeCourseForUpdate();

        try {
            courseService.edit(course);
            viewProvider.printMessage("The course with entered id has been updated.");
        } catch (IncorrectRequestData e) {
            viewProvider.printMessage(e.getMessage());
        }
    }

    private CourseRequest makeCourseForUpdate() {
        CourseRequest courseRequest = new CourseRequest();
        viewProvider.printMessage("\nPlease, type the id of the course you want to update: ");
        courseRequest.setId(viewProvider.readInt());
        viewProvider.printMessage("\nPlease, type course name:");
        courseRequest.setName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type course's description:");
        courseRequest.setDescription(viewProvider.read());

        return courseRequest;
    }

    private void deleteCourse() {
        viewProvider.printMessage("Which course do you want to delete? Please, type the course id: \n");
        int courseId = viewProvider.readInt();

        try {
            courseService.deleteById(courseId);
            viewProvider.printMessage("The course with id = " + courseId + " has been deleted from the database.\n");
        } catch (IncorrectRequestData e) {
            viewProvider.printMessage(e.getMessage());
        }
    }

    private void deleteCourses() {
        Set<Integer> ids = new HashSet<>();
        ids = addIdsForDelete(ids);
        courseService.deleteByIds(ids);
        viewProvider.printMessage("The courses with ids = " + ids + " have been deleted from the database.\n");
    }

    private Set<Integer> addIdsForDelete(Set<Integer> ids) {
        viewProvider.printMessage("Which course do you want to delete? Please, type the course id: \n");
        int courseId = viewProvider.readInt();
        viewProvider.printMessage("\nCourse was added to delete set.");

        viewProvider.printMessage("\nDo you want to add one more course to delete set? Type Y/N (yes/no): ");
        String addMore = viewProvider.read();
        ids.add(courseId);

        if (addMore.equalsIgnoreCase("y")) addIdsForDelete(ids);

        return ids;
    }

    private void findAllCoursesRelateToDepartment() {
        viewProvider.printMessage("Please, type the department id: \n");
        int departmentId = viewProvider.readInt();
        viewProvider.printMessage(courseService.findCoursesRelateToDepartment(departmentId).toString());
    }

    private void findAllCoursesRelateToTeacher() {
        viewProvider.printMessage("Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage(courseService.findCoursesRelateToTeacher(teacherId).toString());
    }

    //    Groups

    private void findGroups() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        String pageNumber = viewProvider.read();
        viewProvider.printMessage(groupService.findAll(pageNumber).toString());
    }

    private void findGroupById() {
        viewProvider.printMessage("\nPlease, type group's id:");
        viewProvider.printMessage(groupService.findById(viewProvider.readInt()).toString());
    }

    private void saveGroup() {
        GroupRequest group = makeNewGroup();
        GroupResponse savedGroup = groupService.register(group);
        viewProvider.printMessage("The course with these parameters has been added to database: \n" +
                savedGroup);
    }

    private GroupRequest makeNewGroup() {
        GroupRequest g = new GroupRequest();
        viewProvider.printMessage("\nPlease, type group name:");
        g.setName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type faculty id:");
        g.setFaculty(Faculty.builder().withId(viewProvider.readInt()).build());
        viewProvider.printMessage("\nPlease, type head student id:");
        g.setHeadStudent(Student.builder().withId(viewProvider.readInt()).build());
        viewProvider.printMessage("\nPlease, type education form id:");
        g.setEducationForm(EducationForm.builder().withId(viewProvider.readInt()).build());

        return g;
    }

    private void deleteGroup() {
        viewProvider.printMessage("Which group do you want to delete? Please, type the group id: \n");
        int groupId = viewProvider.readInt();
        groupService.deleteById(groupId);
        viewProvider.printMessage("The group with id = " + groupId + " has been deleted from database\n");
    }

    private void findGroupsRelateToTeacher() {
        viewProvider.printMessage("\nPlease, type teacher id: ");
        groupService.findGroupsRelateToTeacher(viewProvider.readInt());
    }

    //Departments

    private void findDepartments() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        String pageNumber = viewProvider.read();
        viewProvider.printMessage(departmentService.findAll(pageNumber).toString());
    }

    private void findDepartmentById() {
        viewProvider.printMessage("\nPlease, type department's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(departmentService.findById(id).toString());
    }

    private void saveDepartment() {
        DepartmentRequest department = makeNewDepartment();
        DepartmentResponse savedDepartment = departmentService.register(department);
        viewProvider.printMessage("The teacher with these parameters has been added to database: \n" +
                savedDepartment);
    }

    private DepartmentRequest makeNewDepartment() {
        DepartmentRequest d = new DepartmentRequest();
        viewProvider.printMessage("\nPlease, type department name:");
        d.setName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type description:");
        d.setDescription(viewProvider.read());

        return d;
    }

    private void deleteDepartment() {
        viewProvider.printMessage("Which department do you want to delete? Please, type the department id: \n");
        int departmentId = viewProvider.readInt();
        departmentService.deleteById(departmentId);
        viewProvider.printMessage("The teacher with id = " + departmentId + " has been deleted from database\n");
    }

    //    Lessons

    private void findLessons() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        String pageNumber = viewProvider.read();
        viewProvider.printMessage(lessonService.findAll(pageNumber).toString());
    }

    private void findLessonById() {
        viewProvider.printMessage("\nPlease, type lesson's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(lessonService.findById(id).toString());
    }

    private void saveLesson() {
        LessonRequest lesson = makeNewLesson();
        LessonResponse savedLesson = lessonService.register(lesson);
        viewProvider.printMessage("The lesson with these parameters has been added to database: \n" +
                savedLesson);
    }

    private LessonRequest makeNewLesson() {
        LessonRequest l = new LessonRequest();
        viewProvider.printMessage("\nPlease, type lesson name:");
        l.setName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type group id:");
        l.setGroup(Group.builder().withId(viewProvider.readInt()).build());
        viewProvider.printMessage("\nPlease, type teacher id:");
        l.setTeacher(Teacher.builder().withId(viewProvider.readInt()).build());
        viewProvider.printMessage("\nPlease, type course id:");
        l.setCourse(Course.builder().withId(viewProvider.readInt()).build());
        viewProvider.printMessage("\nPlease, type lesson type id:");
        l.setLessonType(LessonType.builder().withId(viewProvider.readInt()).build());
        l.setStartTime(LocalDateTime.now());
        viewProvider.printMessage("\nStart time will be set automatically as  LocalDateTime.now(): " + LocalDateTime.now());

        return l;
    }

    private void deleteLesson() {
        viewProvider.printMessage("Which lesson do you want to delete? Please, type the lesson id: \n");
        int lessonId = viewProvider.readInt();
        lessonService.deleteById(lessonId);
        viewProvider.printMessage("The lesson with id = " + lessonId + " has been deleted from database\n");
    }

    private void findLessonsRelateToGroup() {
        viewProvider.printMessage("\nPlease, type group id:");
        viewProvider.printMessage(lessonService.findLessonsRelateToGroup(viewProvider.readInt()).toString());
    }

    //Teacher titles

    private void findTeacherTitles() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        String pageNumber = viewProvider.read();
        viewProvider.printMessage(teacherTitleService.findAll(pageNumber).toString());
    }

    private void findTeacherTitleById() {
        viewProvider.printMessage("\nPlease, type teacher title id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(teacherTitleService.findById(id).toString());
    }

    private void saveTeacherTitle() {
        TeacherTitleRequest teacherTitle = makeNewTeacherTitle();
        TeacherTitleResponse savedTeacherTitle = teacherTitleService.register(teacherTitle);
        viewProvider.printMessage("The teacher title with these parameters has been added to database: \n" +
                savedTeacherTitle);
    }

    private TeacherTitleRequest makeNewTeacherTitle() {
        TeacherTitleRequest t = new TeacherTitleRequest();
        viewProvider.printMessage("\nPlease, type teacher title name:");
        t.setName(viewProvider.read());

        return t;
    }

    private void deleteTeacherTitle() {
        viewProvider.printMessage("Which teacher title do you want to delete? Please, type the teacher title id: \n");
        int teacherTitleId = viewProvider.readInt();
        teacherTitleService.deleteById(teacherTitleId);
        viewProvider.printMessage("The teacher title with id = " + teacherTitleId + " has been deleted from database\n");
    }

    //Lesson types

    private void findLessonTypes() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        String pageNumber = viewProvider.read();
        viewProvider.printMessage(teacherTitleService.findAll(pageNumber).toString());
    }

    private void findLessonTypeById() {
        viewProvider.printMessage("\nPlease, type lesson type id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(lessonTypeService.findById(id).toString());
    }

    private void saveLessonType() {
        LessonTypeRequest lessonType = makeNewLessonType();
        LessonTypeResponse savedLessonType = lessonTypeService.register(lessonType);
        viewProvider.printMessage("The lesson type parameters has been added to database: \n" +
                savedLessonType);
    }

    private LessonTypeRequest makeNewLessonType() {
        LessonTypeRequest lt = new LessonTypeRequest();
        viewProvider.printMessage("\nPlease, type lesson type name:");
        lt.setName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type lesson duration in minutes:");
        lt.setDuration(Duration.ofMinutes(viewProvider.readInt()));

        return lt;
    }

    private void deleteLessonType() {
        viewProvider.printMessage("Which lesson type do you want to delete? Please, type the lesson type id: \n");
        int lessonTypeId = viewProvider.readInt();
        lessonTypeService.deleteById(lessonTypeId);
        viewProvider.printMessage("The lesson type with id = " + lessonTypeId + " has been deleted from database\n");
    }

    //    Faculties

    private void findFaculties() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        String pageNumber = viewProvider.read();
        viewProvider.printMessage(facultyService.findAll(pageNumber).toString());
    }

    private void findFacultyById() {
        viewProvider.printMessage("\nPlease, type faculty's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(facultyService.findById(id).toString());
    }

    private void saveFaculty() {
        FacultyRequest faculty = makeNewFaculty();
        FacultyResponse savedFaculty = facultyService.register(faculty);
        viewProvider.printMessage("The faculty with these parameters has been added to database: \n" +
                savedFaculty);
    }

    private FacultyRequest makeNewFaculty() {
        FacultyRequest f = new FacultyRequest();
        viewProvider.printMessage("\nPlease, type faculty's name:");
        f.setName(viewProvider.read());
        viewProvider.printMessage("\nPlease, type faculty's description:");
        f.setDescription(viewProvider.read());

        return f;
    }

    private void deleteFaculty() {
        viewProvider.printMessage("Which faculty do you want to delete? Please, type the faculty id: \n");
        int facultyId = viewProvider.readInt();
        facultyService.deleteById(facultyId);
        viewProvider.printMessage("The faculty with id = " + facultyId + " has been deleted from database\n");
    }

    //    Education Forms

    private void findEducationForms() {
        viewProvider.printMessage("\nPlease, type a page number: ");
        String pageNumber = viewProvider.read();
        viewProvider.printMessage(educationFormService.findAll(pageNumber).toString());
    }

    private void findEducationFormById() {
        viewProvider.printMessage("\nPlease, type education form id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(educationFormService.findById(id).toString());
    }

    private void saveEducationForm() {
        EducationFormRequest educationForm = makeNewEducationForm();
        EducationFormResponse savedEducationForm = educationFormService.register(educationForm);
        viewProvider.printMessage("The education form with these parameters has been added to database: \n" +
                savedEducationForm);
    }

    private EducationFormRequest makeNewEducationForm() {
        EducationFormRequest e = new EducationFormRequest();
        viewProvider.printMessage("\nPlease, type education form name:");
        e.setName(viewProvider.read());

        return e;
    }

    private void deleteEducationForm() {
        viewProvider.printMessage("Which education form do you want to delete? Please, type the education form id: \n");
        int educationFormId = viewProvider.readInt();
        educationFormService.deleteById(educationFormId);
        viewProvider.printMessage("The education form with id = " + educationFormId + " has been deleted from database\n");
    }

//    General

    private void returnToMenu() {
        viewProvider.printMessage("\nPlease, enter any symbol to return to menu\n");
        viewProvider.read();
        startMenu();
    }

}
