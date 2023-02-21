INSERT INTO education_forms (name)
VALUES ('Full-time education'),
       ('Distance education');

INSERT INTO lesson_types (name, duration)
VALUES ('Lecture', 90),
       ('Colloquium', 120),
       ('Laboratory', 90),
       ('Practice', 45),
       ('Long-practice', 90);

INSERT INTO faculties (name, description)
VALUES
-- Mathematics and mechanics department
('Applied Mathematics and Informatics', 'not ready'),
('Mechanics and Mathematical Modeling', 'not ready'),
('Statistics', 'not ready'),
-- Computer and information sciences department
('Mathematics and Computer Sciences', 'not ready'),
('Mathematical Software and Information Systems Administration', 'not ready'),
-- Physics and astronomy department
('Applied Mathematics and Physics', 'not ready'),
('Physics ', 'not ready'),
-- Industrial ecology and biotechnology department
('Biotechnology ', 'not ready'),
('Food Products of Animal Origin', 'not ready'),
('Production Technology and Organization of Catering', 'not ready'),
-- Electrical and heat power engineering department
('Heat Power Engineering', 'not ready'),
('Electrical Power Engineering', 'not ready'),
('Power Machine Engineering', 'not ready'),
('Nuclear Power Engineering and Thermophysics', 'not ready');

INSERT INTO courses (name, description)
VALUES
--* Mathematics and mechanics department
-- Applied Mathematics and Informatics faculty faculty
('Mathematical Modeling and Artificial Intelligence', 'not ready'),
('Software Engineering', 'not ready'),
('Bioinformatics', 'not ready'),
--Mechanics and Mathematical Modeling faculty
('Biomechanics and Medical Engineering', 'not ready'),
('Mechanics and Mathematical Modeling Media with Microstructure', 'not ready'),
('Mathematical modeling of oil and gas production processes', 'not ready'),
--Statistic faculty
('Data analysis in the economy', 'not ready'),

--* Computer and information sciences department
-- Mathematics and Computer Sciences faculty
('Artificial intelligence systems and supercomputer technologies', 'not ready'),
--Mathematical Software and Information Systems Administration faculty
('Intelligent information systems and data processing', 'not ready'),

--* Physics and astronomy department
-- Applied Mathematics and Physics faculty
('Mathematical models and computational technologies in hydroaerodynamics and thermal physics', 'not ready'),
-- Physics faculty
('Nuclear and Elementary Particle Physics', 'not ready'),
('Biochemical Physics', 'not ready'),
('Space Physics', 'not ready'),
('Quantum Nanostructures and Materials', 'not ready'),

--* Industrial ecology and biotechnology department
-- Biotechnology faculty
('Food Biotechnology', 'not ready'),
('Biotechnology (overall profile)', 'not ready'),
-- Food Products of Animal Origin faculty
('Technology and Organization of Production of Raw Meat Food Products', 'not ready'),
-- Production Technology and Organization of Catering faculty
('Production Technology and the Restaurant and Foodservice Industry', 'not ready'),
('Production Technology and Catering', 'not ready'),

--* Electrical and heat power engineering department
-- Heat Power Engineering faculty
('Industrial Heat and Power Engineering', 'not ready'),
-- Electrical Power Engineering faculty
('Electric Power Systems and Networks', 'not ready'),
('Power Stations', 'not ready'),
('Relay protection and automation of electric power systems', 'not ready'),
('Electrical and electronic devices', 'not ready'),
('High-voltage electric power and electrical engineering', 'not ready'),
('Power supply', 'not ready'),
('Electrical equipment and electrical facilities of enterprises, organizations and institutions', 'not ready'),
-- Power Machine Engineering faculty
('Gas turbine, steam turbine installations and engines', 'not ready'),
('Internal Combustion Engines', 'not ready'),
('Hydraulic machines, hydraulic drives and hydropneumoautomatics', 'not ready'),
('Compressor and refrigeration units of the fuel and energy complex', 'not ready'),
('Aircraft engines and power plants', 'not ready'),
('Gas turbine units of gas pumping stations', 'not ready'),
('Turbines and aircraft engines', 'not ready'),
-- Nuclear Power Engineering and Thermophysics faculty
('Nuclear Power Plants and Units', 'not ready');

INSERT INTO teacher_titles (name)
VALUES ('Professor'),
       ('Lecturer'),
       ('Assistant');

INSERT INTO users (user_type, first_name, last_name, gender, email, password, group_id, teacher_title_id, department_id)
VALUES ('student', 'Tom', 'Robinson', 0, 'tomrobinson@gmail.com', '1234', NULL, NULL, NULL),
       ('student','Rory', 'McDonald', 0, 'mcdonald@yandex.ru', '1111', NULL, NULL, NULL),
       ('student','Kate', 'Austin', 1, 'kitty@yahoo.com', 'kate12#', NULL, NULL, NULL),
       ('student','Amanda', 'Johnson', 1, 'amandaj@gmail.com', '0000', NULL, NULL, NULL),
       ('student','Ron', 'Wisley', 0, 'ron@rambler.ru', '43gb', NULL, NULL, NULL),
       ('student','Harry', 'Portnoy', 0, 'gryffindor@hogwarts.com', 'magic7', NULL, NULL, NULL),
       ('student','Lilly', 'Potter', 1, 'lilly@gmail.com', '43gg', NULL, NULL, NULL),
       ('student','Miguel', 'Fernandez', 0, 'fernandez@yahoo.com', '9999', NULL, NULL, NULL),
       ('teacher','Robert', 'Sapolski', 0, 'rsapolski@gmail.com', '3245', NULL, 1, NULL),
       ('teacher','Jordan', 'Pitersen', 0, 'pitersen@mail.ru', 'wo#$', NULL, 2, NULL),
       ('teacher','Ken', 'Wilber', 0, 'ken@gmail.com', '4grt3', NULL, 3, NULL),
       ('teacher','Alexey', 'Semihatov', 0, 'semihatob@gmail.com', '@@jon22', NULL, 1, NULL),
       ('teacher','Kristina', 'Drugova', 1, 'drugova@gmail.com', 'drdrug18', NULL, 2, NULL);

INSERT INTO groups (name, faculty_id, head_user_id, education_form_id)
VALUES ('g1', 1, 1, 2),
       ('g2', 2, 2, 2),
       ('g3', 3, 3, 1),
       ('g4', 4, 4, 1);

UPDATE users SET group_id = 1 WHERE id = 1;
UPDATE users SET group_id = 2 WHERE id = 2;
UPDATE users SET group_id = 3 WHERE id = 3;
UPDATE users SET group_id = 4 WHERE id = 4;
UPDATE users SET group_id = 1 WHERE id = 5;
UPDATE users SET group_id = 3 WHERE id = 6;
UPDATE users SET group_id = 4 WHERE id = 7;
UPDATE users SET group_id = 2 WHERE id = 8;

INSERT INTO user_groups
VALUES (9, 2),
       (9, 3),
       (9, 4),
       (10, 2),
       (10, 3),
       (11, 1),
       (11, 4),
       (12, 3),
       (12, 4),
       (13, 1);

INSERT INTO user_courses
VALUES (9, 2),
       (9, 3),
       (9, 5),
       (10, 8),
       (10, 9),
       (11, 11),
       (11, 14),
       (12, 17),
       (12, 19),
       (13, 20),
       (13, 25),
       (13, 34);

INSERT INTO departments (name, description)
VALUES ('Mathematics and mechanics', 'not ready'),
       ('Computer and information sciences', 'not ready'),
       ('Physics and astronomy', 'not ready'),
       ('Industrial ecology and biotechnology', 'not ready'),
       ('Electrical and heat power engineering', 'not ready');

UPDATE users SET department_id = 1 WHERE id = 9;
UPDATE users SET department_id = 2 WHERE id = 10;
UPDATE users SET department_id = 3 WHERE id = 11;
UPDATE users SET department_id = 4 WHERE id = 12;
UPDATE users SET department_id = 5 WHERE id = 13;

INSERT INTO department_courses
VALUES
--Mathematics and mechanics department
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
-- Computer and information sciences department
(2, 8),
(2, 9),
-- Physics and astronomy department
(3, 10),
(3, 11),
(3, 12),
(3, 13),
(3, 14),
-- Industrial ecology and biotechnology department
(4, 15),
(4, 16),
(4, 17),
(4, 18),
(4, 19),
-- Electrical and heat power engineering department
(5, 20),
(5, 21),
(5, 22),
(5, 23),
(5, 24),
(5, 25),
(5, 26),
(5, 27),
(5, 28),
(5, 29),
(5, 30),
(5, 31),
(5, 32),
(5, 33),
(5, 34),
(5, 35);

INSERT INTO lessons (name, group_id, user_id, course_id, lesson_type_id, start_time)
VALUES ('lesson 1', 1, 9, 1, 1, '2023-06-22 12:00:00'),
       ('lesson 2', 2, 10, 2, 2, '2023-06-22 12:00:00'),
       ('lesson 3', 3, 11, 3, 3, '2023-06-22 12:00:00'),
       ('lesson 4', 4, 12, 6, 1, '2023-06-22 12:00:00'),
       ('lesson 5', 2, 11, 4, 2, '2023-06-22 16:00:00'),
       ('lesson 6', 4, 13, 5, 3, '2023-06-22 16:00:00');
