package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dao.*;
import com.mikhail.tarasevich.university.dao.impl.*;
import com.mikhail.tarasevich.university.entity.*;
import com.mikhail.tarasevich.university.provider.ViewProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FrontController {

    FacultyDaoImpl facultyDao;
    LessonTypeDao lessonTypeDao;
    EducationFormDao educationFormDao;
    CourseDao courseDao;
    GroupDao groupDao;
    LessonDao lessonDao;
    StudentDao studentDao;
    TeacherDao teacherDao;
    DepartmentDao departmentDao;
    TeacherTitleDao teacherTitleDao;
    ViewProvider viewProvider;

    @Autowired
    public FrontController(FacultyDaoImpl facultyDao, LessonTypeDao lessonTypeDao, EducationFormDao educationFormDao,
                           CourseDao courseDao, GroupDao groupDao, LessonDao lessonDao, StudentDao studentDao,
                           TeacherDao teacherDao, DepartmentDao departmentDao, TeacherTitleDao teacherTitleDao,
                           ViewProvider viewProvider) {
        this.facultyDao = facultyDao;
        this.lessonTypeDao = lessonTypeDao;
        this.educationFormDao = educationFormDao;
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.lessonDao = lessonDao;
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
        this.departmentDao = departmentDao;
        this.teacherTitleDao = teacherTitleDao;
        this.viewProvider = viewProvider;
    }

    public void startMenu(int itemsPerPage) {

        int chooser;
        int pages;

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
                        "Press 9 to change teacher's title;\n" +
                        "Press 10 to change teacher's department;\n" +
                        "Press 11 to find all groups relate to teacher;\n" +
                        "Press 12 to find all courses relate to teacher;\n");

                chooser = viewProvider.readInt();

                switch (chooser) {

                    case 0:
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) teacherDao.count() / (double) itemsPerPage);
                        findTeachers(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findTeacherById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveTeacher();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteTeacher();
                        returnToMenu(itemsPerPage);
                        break;

                    case 5:
                        addTeacherToGroup();
                        returnToMenu(itemsPerPage);
                        break;

                    case 6:
                        deleteTeacherFromGroup();
                        returnToMenu(itemsPerPage);
                        break;

                    case 7:
                        addTeacherToCourse();
                        returnToMenu(itemsPerPage);
                        break;

                    case 8:
                        deleteTeacherFromCourse();
                        returnToMenu(itemsPerPage);
                        break;

                    case 9:
                        changeTeacherTitle();
                        returnToMenu(itemsPerPage);
                        break;

                    case 10:
                        changeDepartment();
                        returnToMenu(itemsPerPage);
                        break;

                    case 11:
                        findAllGroupsRelateToTeacher();
                        returnToMenu(itemsPerPage);
                        break;

                    case 12:
                        findAllCoursesRelateToTeacher();
                        returnToMenu(itemsPerPage);
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
                        "Press 5 to change student's group;\n" +
                        "Press 6 to delete student's from a group;\n");

                chooser = viewProvider.readInt();
                switch (chooser) {

                    case 0:
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) studentDao.count() / (double) itemsPerPage);
                        findStudents(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findStudentById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveStudent();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteStudent();
                        returnToMenu(itemsPerPage);
                        break;

                    case 5:
                        addStudentToGroup();
                        returnToMenu(itemsPerPage);
                        break;

                    case 6:
                        deleteStudentFromGroup();
                        returnToMenu(itemsPerPage);
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
                        "Press 4 to delete a course;\n");

                chooser = viewProvider.readInt();
                switch (chooser) {

                    case 0:
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) courseDao.count() / (double) itemsPerPage);
                        findCourses(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findCourseById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveCourse();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteCourse();
                        returnToMenu(itemsPerPage);
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
                        "Press 5 to change faculty in a group;\n" +
                        "Press 6 to change education form in a group;\n" +
                        "Press 7 to change head student in a group;\n");

                chooser = viewProvider.readInt();
                switch (chooser) {

                    case 0:
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) groupDao.count() / (double) itemsPerPage);
                        findGroups(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findGroupById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveGroup();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteGroup();
                        returnToMenu(itemsPerPage);
                        break;

                    case 5:
                        changeFacultyInGroup();
                        returnToMenu(itemsPerPage);
                        break;

                    case 6:
                        changeEducationFormInGroup();
                        returnToMenu(itemsPerPage);
                        break;

                    case 7:
                        changeHeadStudentInGroup();
                        returnToMenu(itemsPerPage);
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
                        "Press 4 to delete a department;\n" +
                        "Press 5 to add course to a department;\n" +
                        "Press 6 to delete course from department;\n" +
                        "Press 7 to find all teachers relate to department;\n" +
                        "Press 8 to find all courses relate to department;\n");

                chooser = viewProvider.readInt();
                switch (chooser) {

                    case 0:
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) departmentDao.count() / (double) itemsPerPage);
                        findDepartments(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findDepartmentById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveDepartment();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteDepartment();
                        returnToMenu(itemsPerPage);
                        break;

                    case 5:
                        addCourseToDepartment();
                        returnToMenu(itemsPerPage);
                        break;

                    case 6:
                        deleteCourseFromDepartment();
                        returnToMenu(itemsPerPage);
                        break;

                    case 7:
                        findAllTeachersRelateToDepartment();
                        returnToMenu(itemsPerPage);
                        break;

                    case 8:
                        findAllCoursesRelateToDepartment();
                        returnToMenu(itemsPerPage);
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
                        "Press 5 to update group in a lesson;\n" +
                        "Press 6 to update teacher in a lesson;\n" +
                        "Press 7 to update course in a lesson;\n" +
                        "Press 8 to update lesson type in a lesson;\n" +
                        "Press 9 to update start time in a lesson;\n");

                chooser = viewProvider.readInt();
                switch (chooser) {

                    case 0:
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) lessonDao.count() / (double) itemsPerPage);
                        findLessons(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findLessonById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveLesson();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteLesson();
                        returnToMenu(itemsPerPage);
                        break;

                    case 5:
                        changeGroupInLesson();
                        returnToMenu(itemsPerPage);
                        break;

                    case 6:
                        changeTeacherInLesson();
                        returnToMenu(itemsPerPage);
                        break;

                    case 7:
                        changeCourseInLesson();
                        returnToMenu(itemsPerPage);
                        break;

                    case 8:
                        changeLessonTypeInLesson();
                        returnToMenu(itemsPerPage);
                        break;

                    case 9:
                        changeStartTimeInLesson();
                        returnToMenu(itemsPerPage);
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
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) teacherTitleDao.count() / (double) itemsPerPage);
                        findTeacherTitles(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findTeacherTitleById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveTeacherTitle();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteTeacherTitle();
                        returnToMenu(itemsPerPage);
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
                        "Press 4 to delete a lesson type;\n" +
                        "Press 5 to change a duration of lesson type;\n");

                chooser = viewProvider.readInt();
                switch (chooser) {

                    case 0:
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) lessonTypeDao.count() / (double) itemsPerPage);
                        findLessonTypes(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findLessonTypeById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveLessonType();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteLessonType();
                        returnToMenu(itemsPerPage);
                        break;

                    case 5:
                        changeDurationInLessonType();
                        returnToMenu(itemsPerPage);
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
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) facultyDao.count() / (double) itemsPerPage);
                        findFaculties(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findFacultyById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveFaculty();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteFaculty();
                        returnToMenu(itemsPerPage);
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
                        startMenu(itemsPerPage);
                        break;

                    case 1:
                        pages = (int) Math.ceil((double) educationFormDao.count() / (double) itemsPerPage);
                        findEducationForms(pages, itemsPerPage);
                        returnToMenu(itemsPerPage);
                        break;

                    case 2:
                        findEducationFormById();
                        returnToMenu(itemsPerPage);
                        break;

                    case 3:
                        saveEducationForm();
                        returnToMenu(itemsPerPage);
                        break;

                    case 4:
                        deleteEducationForm();
                        returnToMenu(itemsPerPage);
                        break;

                    default:
                        break;
                }
                break;

            default:
                startMenu(itemsPerPage);
        }
    }

    //Teachers

    private void findTeachers(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<Teacher> teachers = teacherDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(teachers.toString());
    }

    private void findTeacherById() {
        viewProvider.printMessage("\nPlease, type teacher's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(teacherDao.findById(id).toString());
    }

    private void saveTeacher() {
        Teacher teacher = makeNewTeacher();
        Teacher savedTeacher = teacherDao.save(teacher);
        viewProvider.printMessage("The teacher with these parameters has been added to database: \n" +
                savedTeacher);
    }

    private Teacher makeNewTeacher() {
        viewProvider.printMessage("\nPlease, type first name:");
        String firstName = viewProvider.read();
        viewProvider.printMessage("\nPlease, type last name:");
        String lastName = viewProvider.read();
        viewProvider.printMessage("\nPlease, type gender in digit representation(MALE = 0 /FEMALE = 1):\n");
        Gender gender = Gender.getById(viewProvider.readInt());
        viewProvider.printMessage("\nPlease, type email:");
        String email = viewProvider.read();
        viewProvider.printMessage("\nPlease, type password:");
        String password = viewProvider.read();
        viewProvider.printMessage("\nPlease, type teacherTitleId:");
        int teacherTitleId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type departmentId:");
        int departmentId = viewProvider.readInt();

        return Teacher.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withGender(gender)
                .withEmail(email)
                .withPassword(password)
                .withTeacherTitle(TeacherTitle.builder().withId(teacherTitleId).build())
                .withDepartment(Department.builder().withId(departmentId).build())
                .build();
    }

    private void deleteTeacher() {
        viewProvider.printMessage("Which teacher do you want to delete? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        teacherDao.deleteById(teacherId);
        viewProvider.printMessage("The teacher with id = " + teacherId + " has been deleted from database\n");
    }

    private void addTeacherToGroup() {
        viewProvider.printMessage("Which teacher do you want to add to the group? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the group id: \n");
        int groupId = viewProvider.readInt();
        teacherDao.addUserToGroup(teacherId, groupId);
    }

    private void deleteTeacherFromGroup() {
        viewProvider.printMessage("Which teacher do you want to delete from the group? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the group id: \n");
        int groupId = viewProvider.readInt();
        teacherDao.deleteTeacherFromGroup(teacherId, groupId);
    }

    private void addTeacherToCourse() {
        viewProvider.printMessage("Which teacher do you want to add to the course? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the course id: \n");
        int courseId = viewProvider.readInt();
        teacherDao.addTeacherToCourse(teacherId, courseId);
    }

    private void deleteTeacherFromCourse() {
        viewProvider.printMessage("Which teacher do you want to delete from the course? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the course id: \n");
        int courseId = viewProvider.readInt();
        teacherDao.deleteTeacherFromCourse(teacherId, courseId);
    }

    private void changeTeacherTitle() {
        viewProvider.printMessage("Which teacher do you want to change the teacher title? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the teacher title id: \n");
        int teacherTitle = viewProvider.readInt();
        teacherDao.changeTeacherTitle(teacherId, teacherTitle);
    }

    private void changeDepartment() {
        viewProvider.printMessage("Which teacher do you want to change the department? Please, type the teacher id: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the department id: \n");
        int departmentId = viewProvider.readInt();
        teacherDao.deleteTeacherFromCourse(teacherId, departmentId);
    }

    private void findAllGroupsRelateToTeacher() {
        viewProvider.printMessage("Please, type the teacher id which groups you want to find: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage(groupDao.findGroupsRelateToTeacher(teacherId).toString());
    }

    private void findAllCoursesRelateToTeacher() {
        viewProvider.printMessage("Please, type the teacher id which courses you want to find: \n");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage(courseDao.findCoursesRelateToTeacher(teacherId).toString());
    }

//    Students

    private void findStudents(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<Student> students = studentDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(students.toString());
    }

    private void findStudentById() {
        viewProvider.printMessage("\nPlease, type student's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(studentDao.findById(id).toString());
    }

    private void saveStudent() {
        Student student = makeNewStudent();
        Student savedStudent = studentDao.save(student);
        viewProvider.printMessage("The student with these parameters has been added to database: \n" +
                savedStudent);
    }

    private Student makeNewStudent() {
        viewProvider.printMessage("\nPlease, type first name:");
        String firstName = viewProvider.read();
        viewProvider.printMessage("\nPlease, type last name:");
        String lastName = viewProvider.read();
        viewProvider.printMessage("\nPlease, type gender in digit representation(MALE = 0 /FEMALE = 1):\n");
        Gender gender = Gender.getById(viewProvider.readInt());
        viewProvider.printMessage("\nPlease, type email:");
        String email = viewProvider.read();
        viewProvider.printMessage("\nPlease, type password:");
        String password = viewProvider.read();
        viewProvider.printMessage("\nPlease, type groupId:");
        int groupId = viewProvider.readInt();

        return Student.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withGender(gender)
                .withEmail(email)
                .withPassword(password)
                .withGroup(Group.builder().withId(groupId).build())
                .build();
    }

    private void deleteStudent() {
        viewProvider.printMessage("Which student do you want to delete? Please, type the student id: \n");
        int studentId = viewProvider.readInt();
        studentDao.deleteById(studentId);
        viewProvider.printMessage("The student with id = " + studentId + " has been deleted from database\n");
    }

    private void addStudentToGroup() {
        viewProvider.printMessage("Which student do you want to add to the group? Please, type the student id: \n");
        int studentId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the group id: \n");
        int groupId = viewProvider.readInt();
        studentDao.addUserToGroup(studentId, groupId);
    }

    private void deleteStudentFromGroup() {
        viewProvider.printMessage("Which student do you want to delete from the group? Please, type the student id: \n");
        int studentId = viewProvider.readInt();
        studentDao.deleteStudentFromGroup(studentId);
    }

    //    Courses

    private void findCourses(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<Course> courses = courseDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(courses.toString());
    }

    private void findCourseById() {
        viewProvider.printMessage("\nPlease, type course's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(courseDao.findById(id).toString());
    }

    private void saveCourse() {
        Course course = makeNewCourse();
        Course savedCourse = courseDao.save(course);
        viewProvider.printMessage("The course with these parameters has been added to database: \n" +
                savedCourse);
    }

    private Course makeNewCourse() {
        viewProvider.printMessage("\nPlease, type course name:");
        String name = viewProvider.read();
        viewProvider.printMessage("\nPlease, type course's description:");
        String description = viewProvider.read();

        return Course.builder()
                .withName(name)
                .withDescription(description)
                .build();
    }

    private void deleteCourse() {
        viewProvider.printMessage("Which course do you want to delete? Please, type the course id: \n");
        int courseId = viewProvider.readInt();
        courseDao.deleteById(courseId);
        viewProvider.printMessage("The course with id = " + courseId + " has been deleted from database\n");
    }

    //    Groups

    private void findGroups(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<Group> groups = groupDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(groups.toString());
    }

    private void findGroupById() {
        viewProvider.printMessage("\nPlease, type group's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(groupDao.findById(id).toString());
    }

    private void saveGroup() {
        Group group = makeNewGroup();
        Group savedGroup = groupDao.save(group);
        viewProvider.printMessage("The course with these parameters has been added to database: \n" +
                savedGroup);
    }

    private Group makeNewGroup() {
        viewProvider.printMessage("\nPlease, type group name:");
        String name = viewProvider.read();
        viewProvider.printMessage("\nPlease, type faculty id:");
        int facultyId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type head student id:");
        int headStudentId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type education form id:");
        int educationFormId = viewProvider.readInt();

        return Group.builder()
                .withName(name)
                .withFaculty(Faculty.builder().withId(facultyId).build())
                .withHeadStudent(Student.builder().withId(headStudentId).build())
                .withEducationForm(EducationForm.builder().withId(educationFormId).build())
                .build();
    }

    private void deleteGroup() {
        viewProvider.printMessage("Which group do you want to delete? Please, type the group id: \n");
        int groupId = viewProvider.readInt();
        groupDao.deleteById(groupId);
        viewProvider.printMessage("The group with id = " + groupId + " has been deleted from database\n");
    }

    private void changeFacultyInGroup() {
        viewProvider.printMessage("\nPlease, type group id where you want to change faculty id:");
        int groupId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type faculty id:");
        int facultyId = viewProvider.readInt();
        groupDao.changeFaculty(groupId, facultyId);
        viewProvider.printMessage("Changes was successful");
    }

    private void changeHeadStudentInGroup() {
        viewProvider.printMessage("\nPlease, type group id where you want to change head student id:");
        int groupId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type new head student id:");
        int studentId = viewProvider.readInt();
        groupDao.changeHeadUser(groupId, studentId);
        viewProvider.printMessage("Changes was successful");
    }

    private void changeEducationFormInGroup() {
        viewProvider.printMessage("\nPlease, type group id where you want to change education form id:");
        int groupId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type new education form id:");
        int educationFormId = viewProvider.readInt();
        groupDao.changeEducationForm(groupId, educationFormId);
        viewProvider.printMessage("Changes was successful");
    }

    //Departments

    private void findDepartments(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<Department> departments = departmentDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(departments.toString());
    }

    private void findDepartmentById() {
        viewProvider.printMessage("\nPlease, type department's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(departmentDao.findById(id).toString());
    }

    private void saveDepartment() {
        Department department = makeNewDepartment();
        Department savedDepartment = departmentDao.save(department);
        viewProvider.printMessage("The teacher with these parameters has been added to database: \n" +
                savedDepartment);
    }

    private Department makeNewDepartment() {
        viewProvider.printMessage("\nPlease, type department name:");
        String name = viewProvider.read();
        viewProvider.printMessage("\nPlease, type description:");
        String description = viewProvider.read();

        return Department.builder()
                .withName(name)
                .withDescription(description)
                .build();
    }

    private void deleteDepartment() {
        viewProvider.printMessage("Which department do you want to delete? Please, type the department id: \n");
        int departmentId = viewProvider.readInt();
        departmentDao.deleteById(departmentId);
        viewProvider.printMessage("The teacher with id = " + departmentId + " has been deleted from database\n");
    }

    private void addCourseToDepartment() {
        viewProvider.printMessage("Which course do you want to add to the department? Please, type the course id: \n");
        int courseId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the department id: \n");
        int departmentId = viewProvider.readInt();
        departmentDao.addCourseToDepartment(departmentId, courseId);
    }

    private void deleteCourseFromDepartment() {
        viewProvider.printMessage("Which course do you want to delete from the department? Please, type the course id: \n");
        int courseId = viewProvider.readInt();
        viewProvider.printMessage("Please, type the department id: \n");
        int departmentId = viewProvider.readInt();
        departmentDao.deleteCourseFromDepartment(departmentId, courseId);
    }

    private void findAllTeachersRelateToDepartment() {
        viewProvider.printMessage("Please, type the department id: \n");
        int departmentId = viewProvider.readInt();
        viewProvider.printMessage(teacherDao.findTeachersRelateToDepartment(departmentId).toString());
    }

    private void findAllCoursesRelateToDepartment() {
        viewProvider.printMessage("Please, type the department id: \n");
        int departmentId = viewProvider.readInt();
        viewProvider.printMessage(courseDao.findCoursesRelateToDepartment(departmentId).toString());
    }

    //    Lessons

    private void findLessons(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<Lesson> lessons = lessonDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(lessons.toString());
    }

    private void findLessonById() {
        viewProvider.printMessage("\nPlease, type lesson's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(lessonDao.findById(id).toString());
    }

    private void saveLesson() {
        Lesson lesson = makeNewLesson();
        Lesson savedLesson = lessonDao.save(lesson);
        viewProvider.printMessage("The lesson with these parameters has been added to database: \n" +
                savedLesson);
    }

    private Lesson makeNewLesson() {
        viewProvider.printMessage("\nPlease, type lesson name:");
        String name = viewProvider.read();
        viewProvider.printMessage("\nPlease, type group id:");
        int groupId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type teacher id:");
        int teacherId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type course id:");
        int courseId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type lesson type id:");
        int lessonTypeId = viewProvider.readInt();
        LocalDateTime now = LocalDateTime.now();
        viewProvider.printMessage("\nStart time will be set automatically as  LocalDateTime.now(): " + now);

        return Lesson.builder()
                .withName(name)
                .withGroup(Group.builder().withId(groupId).build())
                .withTeacher(Teacher.builder().withId(teacherId).build())
                .withCourse(Course.builder().withId(courseId).build())
                .withLessonType(LessonType.builder().withId(lessonTypeId).build())
                .withStartTime(now)
                .build();
    }

    private void deleteLesson() {
        viewProvider.printMessage("Which lesson do you want to delete? Please, type the lesson id: \n");
        int lessonId = viewProvider.readInt();
        lessonDao.deleteById(lessonId);
        viewProvider.printMessage("The lesson with id = " + lessonId + " has been deleted from database\n");
    }

    private void changeGroupInLesson() {
        viewProvider.printMessage("\nPlease, type lesson id where you want to change group id:");
        int lessonId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type group id:");
        int groupId = viewProvider.readInt();
        lessonDao.changeGroup(lessonId, groupId);
        viewProvider.printMessage("Changes was successful");
    }

    private void changeTeacherInLesson() {
        viewProvider.printMessage("\nPlease, type lesson id where you want to change teacher id:");
        int lessonId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type teacher id:");
        int teacherId = viewProvider.readInt();
        lessonDao.changeTeacher(lessonId, teacherId);
        viewProvider.printMessage("Changes was successful");
    }

    private void changeCourseInLesson() {
        viewProvider.printMessage("\nPlease, type lesson id where you want to change course id:");
        int lessonId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type course id:");
        int teacherId = viewProvider.readInt();
        lessonDao.changeCourse(lessonId, teacherId);
        viewProvider.printMessage("Changes was successful");
    }

    private void changeLessonTypeInLesson() {
        viewProvider.printMessage("\nPlease, type lesson id where you want to change lesson type id:");
        int lessonId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type lesson type id:");
        int lessonTypeId = viewProvider.readInt();
        lessonDao.changeLessonType(lessonId, lessonTypeId);
        viewProvider.printMessage("Changes was successful");
    }

    private void changeStartTimeInLesson() {
        viewProvider.printMessage("\nPlease, type lesson id where you want to change start time:");
        int lessonId = viewProvider.readInt();
        LocalDateTime now = LocalDateTime.now();
        viewProvider.printMessage("\nStart time will be set automatically as LocalDateTime.now(): " + now);
        lessonDao.changeStartTime(lessonId, now);
        viewProvider.printMessage("Changes was successful");
    }

    //Teacher titles

    private void findTeacherTitles(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<TeacherTitle> teacherTitles = teacherTitleDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(teacherTitles.toString());
    }

    private void findTeacherTitleById() {
        viewProvider.printMessage("\nPlease, type teacher title id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(teacherTitleDao.findById(id).toString());
    }

    private void saveTeacherTitle() {
        TeacherTitle teacherTitle = makeNewTeacherTitle();
        TeacherTitle savedTeacherTitle = teacherTitleDao.save(teacherTitle);
        viewProvider.printMessage("The teacher title with these parameters has been added to database: \n" +
                savedTeacherTitle);
    }

    private TeacherTitle makeNewTeacherTitle() {
        viewProvider.printMessage("\nPlease, type teacher title name:");
        String name = viewProvider.read();

        return TeacherTitle.builder()
                .withName(name)
                .build();
    }

    private void deleteTeacherTitle() {
        viewProvider.printMessage("Which teacher title do you want to delete? Please, type the teacher title id: \n");
        int teacherTitleId = viewProvider.readInt();
        teacherTitleDao.deleteById(teacherTitleId);
        viewProvider.printMessage("The teacher title with id = " + teacherTitleId + " has been deleted from database\n");
    }

    //Lesson types

    private void findLessonTypes(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<LessonType> lessonTypes = lessonTypeDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(lessonTypes.toString());
    }

    private void findLessonTypeById() {
        viewProvider.printMessage("\nPlease, type lesson type id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(lessonTypeDao.findById(id).toString());
    }

    private void saveLessonType() {
        LessonType lessonType = makeNewLessonType();
        LessonType savedLessonType = lessonTypeDao.save(lessonType);
        viewProvider.printMessage("The lesson type parameters has been added to database: \n" +
                savedLessonType);
    }

    private LessonType makeNewLessonType() {
        viewProvider.printMessage("\nPlease, type lesson type name:");
        String name = viewProvider.read();
        viewProvider.printMessage("\nPlease, type lesson duration in minutes:");
        Duration duration = Duration.ofMinutes(viewProvider.readInt());

        return LessonType.builder()
                .withName(name)
                .withDuration(duration)
                .build();
    }

    private void deleteLessonType() {
        viewProvider.printMessage("Which lesson type do you want to delete? Please, type the lesson type id: \n");
        int lessonTypeId = viewProvider.readInt();
        lessonTypeDao.deleteById(lessonTypeId);
        viewProvider.printMessage("The lesson type with id = " + lessonTypeId + " has been deleted from database\n");
    }

    private void changeDurationInLessonType() {
        viewProvider.printMessage("\nPlease, type lesson type id where you want to change duration:");
        int lessonTypeId = viewProvider.readInt();
        viewProvider.printMessage("\nPlease, type new lesson duration in minutes:");
        Duration duration = Duration.ofMinutes(viewProvider.readInt());
        lessonTypeDao.changeDuration(lessonTypeId, duration);
        viewProvider.printMessage("Changes was successful");
    }

    //    Faculties

    private void findFaculties(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<Faculty> faculties = facultyDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(faculties.toString());
    }

    private void findFacultyById() {
        viewProvider.printMessage("\nPlease, type faculty's id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(facultyDao.findById(id).toString());
    }

    private void saveFaculty() {
        Faculty faculty = makeNewFaculty();
        Faculty savedFaculty = facultyDao.save(faculty);
        viewProvider.printMessage("The faculty with these parameters has been added to database: \n" +
                savedFaculty);
    }

    private Faculty makeNewFaculty() {
        viewProvider.printMessage("\nPlease, type faculty's name:");
        String name = viewProvider.read();
        viewProvider.printMessage("\nPlease, type faculty's description:");
        String description = viewProvider.read();

        return Faculty.builder()
                .withName(name)
                .withDescription(description)
                .build();
    }

    private void deleteFaculty() {
        viewProvider.printMessage("Which faculty do you want to delete? Please, type the faculty id: \n");
        int facultyId = viewProvider.readInt();
        facultyDao.deleteById(facultyId);
        viewProvider.printMessage("The faculty with id = " + facultyId + " has been deleted from database\n");
    }

    //    Education Forms

    private void findEducationForms(int pages, int itemsPerPage) {
        viewProvider.printMessage("\nPlease, type a page number from 1 to " + pages + ":");
        int pageNumber = viewProvider.readInt();
        if (pageNumber <= 0 || pageNumber > pages) {
            viewProvider.printMessage("You typed an incorrect page number, please, try again\n");
            return;
        }
        List<EducationForm> educationForms = educationFormDao.findAll(pageNumber - 1, itemsPerPage);
        viewProvider.printMessage(educationForms.toString());
    }

    private void findEducationFormById() {
        viewProvider.printMessage("\nPlease, type education form id:");
        int id = viewProvider.readInt();
        viewProvider.printMessage(educationFormDao.findById(id).toString());
    }

    private void saveEducationForm() {
        EducationForm educationForm = makeNewEducationForm();
        EducationForm savedEducationForm = educationFormDao.save(educationForm);
        viewProvider.printMessage("The education form with these parameters has been added to database: \n" +
                savedEducationForm);
    }

    private EducationForm makeNewEducationForm() {
        viewProvider.printMessage("\nPlease, type education form name:");
        String name = viewProvider.read();

        return EducationForm.builder()
                .withName(name)
                .build();
    }

    private void deleteEducationForm() {
        viewProvider.printMessage("Which education form do you want to delete? Please, type the education form id: \n");
        int educationFormId = viewProvider.readInt();
        educationFormDao.deleteById(educationFormId);
        viewProvider.printMessage("The education form with id = " + educationFormId + " has been deleted from database\n");
    }


//    General

    private void returnToMenu(int itemsPerPage) {
        viewProvider.printMessage("\nPlease, enter any symbol to return to menu\n");
        viewProvider.read();
        startMenu(itemsPerPage);
    }
}
