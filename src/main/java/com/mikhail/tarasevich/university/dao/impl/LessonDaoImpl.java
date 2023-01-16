package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

@Repository
public class LessonDaoImpl extends AbstractPageableCrudDaoImpl<Lesson> implements LessonDao {

    private static final String SAVE_QUERY =
            "INSERT INTO lessons (name, group_id, user_id, course_id, lesson_type_id, start_time) " +
                    "VALUES(?, ?, ?, ?, ?, ?)";
    private static final String FIND_COMMON_PART_QUERY = "SELECT lessons.id AS lesson_id, " +
            "lessons.name AS lesson_name, groups.id AS group_id, groups.name AS group_name, " +
            "users.id AS user_id, first_name, last_name, gender, email, password, teacher_title_id, department_id, " +
            "courses.id AS course_id, courses.name AS course_name, description, " +
            "lesson_types.id AS lesson_type_id, lesson_types.name AS lesson_type_name, duration, start_time " +
            "FROM lessons " +
            "LEFT JOIN groups ON group_id = groups.id " +
            "LEFT JOIN users ON user_id = users.id " +
            "LEFT JOIN courses ON course_id = courses.id " +
            "LEFT JOIN lesson_types ON lesson_type_id = lesson_types.id ";
    private static final String FIND_ALL_QUERY = FIND_COMMON_PART_QUERY + "ORDER BY lessons.id";
    private static final String FIND_BY_ID_QUERY =
            FIND_COMMON_PART_QUERY + "WHERE lessons.id = ? ORDER BY lessons.id";
    private static final String FIND_ALL_PAGEABLE_QUERY =
            FIND_COMMON_PART_QUERY + "ORDER BY lessons.id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE lessons SET name = ?, group_id = ?, user_id = ?, " +
            "course_id = ?, lesson_type_id = ?, start_time = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM lessons WHERE id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM lessons";
    private static final String UPDATE_GROUP_QUERY = "UPDATE lessons SET group_id = ? WHERE id = ?";
    private static final String UPDATE_TEACHER_QUERY = "UPDATE lessons SET user_id = ? WHERE id = ?";
    private static final String UPDATE_COURSE_QUERY = "UPDATE lessons SET course_id = ? WHERE id = ?";
    private static final String UPDATE_LESSON_TYPE_QUERY = "UPDATE lessons SET lesson_type_id = ? WHERE id = ?";
    private static final String UPDATE_START_TIME_QUERY = "UPDATE lessons SET start_time = ? WHERE id = ?";
    private static final RowMapper<Lesson> ROW_MAPPER = (resultSet, rowNum) ->
            Lesson.builder()
                    .withId(resultSet.getInt("lesson_id"))
                    .withName(resultSet.getString("lesson_name"))
                    .withGroup(Group.builder()
                            .withId(resultSet.getInt("group_id"))
                            .withName(resultSet.getString("group_name"))
                            .build())
                    .withTeacher(Teacher.builder()
                            .withId(resultSet.getInt("user_id"))
                            .withFirstName(resultSet.getString("first_name"))
                            .withLastName(resultSet.getString("last_name"))
                            .withGender(Gender.getById(resultSet.getInt("gender")))
                            .withEmail(resultSet.getString("email"))
                            .withPassword(resultSet.getString("password"))
                            .withTeacherTitle(TeacherTitle.builder()
                                    .withId(resultSet.getInt("teacher_title_id"))
                                    .build())
                            .withDepartment(Department.builder()
                                    .withId(resultSet.getInt("department_id"))
                                    .build())
                            .build())
                    .withCourse(Course.builder()
                            .withId(resultSet.getInt("course_id"))
                            .withName(resultSet.getString("course_name"))
                            .withDescription(resultSet.getString("description"))
                            .build())
                    .withLessonType(LessonType.builder()
                            .withId(resultSet.getInt("lesson_type_id"))
                            .withName(resultSet.getString("lesson_type_name"))
                            .withDuration(Duration.ofMinutes(resultSet.getLong("duration")))
                            .build())
                    .withStartTime(resultSet.getTimestamp("start_time").toLocalDateTime())
                    .build();

    @Autowired
    public LessonDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY,
                FIND_ALL_PAGEABLE_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    public void changeGroup(int lessonId, int newGroupId) {
        jdbcTemplate.update(UPDATE_GROUP_QUERY, newGroupId, lessonId);
    }

    @Override
    public void changeTeacher(int lessonId, int newTeacherId) {
        jdbcTemplate.update(UPDATE_TEACHER_QUERY, newTeacherId, lessonId);
    }

    @Override
    public void changeCourse(int lessonId, int newCourseId) {
        jdbcTemplate.update(UPDATE_COURSE_QUERY, newCourseId, lessonId);
    }

    @Override
    public void changeLessonType(int lessonId, int newLessonTypeId) {
        jdbcTemplate.update(UPDATE_LESSON_TYPE_QUERY, newLessonTypeId, lessonId);
    }

    @Override
    public void changeStartTime(int lessonId, LocalDateTime newStartTime) {
        jdbcTemplate.update(UPDATE_START_TIME_QUERY, newStartTime, lessonId);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, Lesson entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getGroup().getId());
        ps.setInt(3, entity.getTeacher().getId());
        ps.setInt(4, entity.getCourse().getId());
        ps.setInt(5, entity.getLessonType().getId());
        ps.setObject(6, entity.getStartTime());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, Lesson entity) throws SQLException {
        setStatementForSave(ps, entity);
        ps.setInt(7, entity.getId());
    }

    @Override
    protected Lesson makeEntityWithId(Lesson entity, int id) {
        return Lesson.builder()
                .withId(id)
                .withName(entity.getName())
                .withGroup(entity.getGroup())
                .withTeacher(entity.getTeacher())
                .withCourse(entity.getCourse())
                .withLessonType(entity.getLessonType())
                .withStartTime(entity.getStartTime())
                .build();
    }

}
