INSERT INTO education_forms (name)
VALUES ('Full-time education'),
       ('Distance education'),
       ('test EF 1'),
       ('test EF 2');


INSERT INTO lesson_types (name, duration)
VALUES ('Lecture', 90),
       ('Practice', 45),
       ('test LT 1', 60),
       ('test LT 2', 120);

INSERT INTO faculties (name, description)
VALUES
-- Mathematics and mechanics department
('Applied Mathematics and Informatics', 'without description'),
('Mechanics and Mathematical Modeling', 'without description'),
('Statistics', 'without description'),
-- Computer and information sciences department
('Mathematics and Computer Sciences', 'without description'),
('Mathematical Software and Information Systems Administration', 'without description'),
('test faculty 1', 'without description'),
('test faculty 2', 'without description');

INSERT INTO courses (name, description)
VALUES
--* Mathematics and mechanics department
-- Applied Mathematics and Informatics faculty faculty
('Mathematical Modeling and Artificial Intelligence', 'without description'),
('Software Engineering', 'without description'),
('Bioinformatics', 'without description'),
--Statistic faculty
('Data analysis in the economy', 'without description'),

--* Computer and information sciences department
-- Mathematics and Computer Sciences faculty
('Artificial intelligence systems and supercomputer technologies', 'without description'),
--Mathematical Software and Information Systems Administration faculty
('Intelligent information systems and data processing', 'without description'),
('test course 1', 'without description'),
('test course 2', 'without description');

INSERT INTO teacher_titles (name)
VALUES ('Professor'),
       ('Lecturer'),
       ('test TT 1'),
       ('test TT 2');

INSERT INTO users (user_type, first_name, last_name, gender, email, password, group_id, teacher_title_id, department_id)
VALUES ('student', 'Tom', 'Robinson', 0, 'tomrobinson@gmail.com', '1234', NULL, NULL, NULL),
       ('student', 'Rory', 'McDonald', 0, 'mcdonald@yandex.ru', '1111', NULL, NULL, NULL),
       ('student', 'Kate', 'Austin', 1, 'kitty@yahoo.com', 'kate12#', NULL, NULL, NULL),
       ('student', 'Amanda', 'Johnson', 1, 'amandaj@gmail.com', '0000', NULL, NULL, NULL),
       ('teacher', 'Robert', 'Sapolski', 0, 'rsapolski@gmail.com', '3245', NULL, 1, NULL),
       ('teacher', 'Kristina', 'Drugova', 1, 'drugova@gmail.com', 'drdrug18', NULL, 2, NULL),
       ('student', 'testName1', 'testLastName1', 0, 'test1@gmail.com', 'test1', NULL, NULL, NULL),
       ('student', 'testName2', 'testLastName2', 1, 'test2@gmail.com', 'test2', NULL, NULL, NULL),
       ('teacher', 'testName3', 'testLastName3', 0, 'test3@gmail.com', 'test3', NULL, 1, NULL),
       ('teacher', 'testName4', 'testLastName4', 1, 'test4@gmail.com', 'test4', NULL, 2, NULL);

INSERT INTO groups (name, faculty_id, head_user_id, education_form_id)
VALUES ('g1', 1, 1, 2),
       ('g2', 2, 2, 2),
       ('g3', 3, 3, 1),
       ('g4', 4, 4, 1),
       ('test group 1', NULL, NULL, NULL),
       ('test group 2', NULL, NULL, NULL);

UPDATE users SET group_id = 1 WHERE id = 1;
UPDATE users SET group_id = 2 WHERE id = 2;
UPDATE users SET group_id = 3 WHERE id = 3;
UPDATE users SET group_id = 4 WHERE id = 4;
UPDATE users SET group_id = 1 WHERE id = 7;
UPDATE users SET group_id = 1 WHERE id = 8;

INSERT INTO user_groups
VALUES (5, 1),
       (5, 2),
       (6, 3),
       (6, 4);

INSERT INTO user_courses
VALUES (5, 1),
       (5, 2),
       (5, 3),
       (5, 4),
       (6, 5),
       (6, 6);

INSERT INTO departments (name, description)
VALUES ('Mathematics and mechanics', 'without description'),
       ('Computer and information sciences', 'without description'),
       ('test department 1', 'without description'),
       ('test department 2', 'without description');

UPDATE users SET department_id = 1 WHERE id = 5;
UPDATE users SET department_id = 2 WHERE id = 6;
UPDATE users SET department_id = 1 WHERE id = 9;
UPDATE users SET department_id = 2 WHERE id = 10;

INSERT INTO department_courses
VALUES
--Mathematics and mechanics department
(1, 1),
(1, 2),
(1, 3),
(1, 4),
-- Computer and information sciences department
(2, 5),
(2, 6);

INSERT INTO lessons (name, group_id, user_id, course_id, lesson_type_id, start_time)
VALUES ('lesson 1', 1, 5, 1, 1, '2023-06-22 12:00:00'),
       ('lesson 2', 2, 6, 5, 2, '2023-06-22 12:00:00');
