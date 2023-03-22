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
('Applied Mathematics and Informatics',
 'This faculty focuses on the application of mathematics and computer science in various fields, such as physics, economics, and engineering.'),

-- Computer and information sciences department
('Mathematics and Computer Sciences',
 'This faculty combines mathematics and computer science to solve complex problems in various fields, including engineering, physics, and economics.'),

-- Physics and astronomy department
('Applied Mathematics and Physics',
 'This faculty focuses on the application of mathematical and computational methods to solve problems in physics, mechanics, and engineering.'),

-- Industrial ecology and biotechnology department
('Biotechnology ',
 'This faculty is dedicated to the study of living organisms and the development of technologies based on their biological systems.'),

-- Electrical and heat power engineering department
('Heat Power Engineering',
 'This faculty is dedicated to the study of thermal power engineering, including the production, distribution, and use of thermal energy.');

INSERT INTO courses (name, description)
VALUES
--* Mathematics and mechanics department
-- Applied Mathematics and Informatics faculty faculty
('Mathematical Modeling and Artificial Intelligence',
 'This course focuses on the principles and techniques of mathematical modeling and artificial intelligence, including mathematical optimization, statistical learning, and neural networks. Students will learn to apply these techniques to real-world problems in various fields.'),
('Software Engineering',
 'This course covers the principles and practices of software engineering, including software design, development, testing, and maintenance. Students will learn to apply software engineering methodologies to develop complex software systems.'),
('Bioinformatics',
 'This course explores the intersection of biology, computer science, and statistics. Students will learn to analyze and interpret biological data using computational methods and to develop algorithms and software tools for analyzing biological data.'),
--Mechanics and Mathematical Modeling faculty
('Biomechanics and Medical Engineering',
 'This course covers the principles of biomechanics and medical engineering, including the mechanical properties of biological tissues, the mechanics of the human body, and the design and development of medical devices.'),
('Mechanics and Mathematical Modeling Media with Microstructure',
 'This course focuses on the mechanics and mathematical modeling of materials with microstructure, including composite materials, ceramics, and metals. Students will learn to analyze the mechanical behavior of these materials using mathematical models.'),
('Mathematical modeling of oil and gas production processes',
 'This course covers the principles of mathematical modeling of oil and gas production processes, including reservoir engineering, well testing, and production optimization. Students will learn to develop mathematical models to simulate and optimize oil and gas production processes.'),
--Statistic faculty
('Data analysis in the economy',
 'This course covers the principles of statistical analysis and modeling in economics, including regression analysis, time series analysis, and panel data analysis. Students will learn to apply these techniques to analyze and interpret economic data.'),
--* Computer and information sciences department
-- Mathematics and Computer Sciences faculty
('Artificial intelligence systems and supercomputer technologies',
 'This course covers the principles and practices of artificial intelligence systems and supercomputer technologies. Students will learn to develop intelligent systems and applications using advanced algorithms and techniques, including neural networks, genetic algorithms, and fuzzy logic.'),
--Mathematical Software and Information Systems Administration faculty
('Intelligent information systems and data processing',
 'This course covers the principles of intelligent information systems and data processing, including knowledge representation, expert systems, and natural language processing. Students will learn to develop intelligent information systems and applications using advanced techniques in artificial intelligence and natural language processing.'),
--* Physics and astronomy department
-- Applied Mathematics and Physics faculty
('Mathematical models and computational technologies in hydroaerodynamics and thermal physics',
 'This course covers the principles of mathematical modeling and computational technologies in hydroaerodynamics and thermal physics, including computational fluid dynamics and heat transfer. Students will learn to develop mathematical models and simulations to analyze and predict the behavior of fluids and heat in various systems.'),
-- Physics faculty
('Nuclear and Elementary Particle Physics',
 'This course covers the fundamental principles of nuclear and elementary particle physics, including the structure and properties of atomic nuclei, radioactive decay, nuclear reactions, and the behavior of subatomic particles.'),
('Biochemical Physics',
 'This course explores the physical principles that underlie the structure and function of biomolecules, including proteins, nucleic acids, and membranes.'),
('Space Physics',
 'This course examines the physics of the space environment, including the interaction of solar and cosmic radiation with the Earth’s magnetic field, the aurora, and space weather.'),
('Quantum Nanostructures and Materials',
 'This course covers the principles of quantum mechanics and their application to the behavior of nanostructures and materials.'),

--* Industrial ecology and biotechnology department
-- Biotechnology faculty
('Food Biotechnology',
 'This course explores the use of biotechnology in food production, including genetic engineering, fermentation, and enzyme technology.'),
('Biotechnology (overall profile)',
 'This course provides an overview of biotechnology and its applications in medicine, agriculture, and industry.'),
-- Food Products of Animal Origin faculty
('Technology and Organization of Production of Raw Meat Food Products',
 'This course covers the production and processing of raw meat food products, including meat hygiene, slaughter, and meat processing techniques.'),
-- Production Technology and Organization of Catering faculty
('Production Technology and the Restaurant and Foodservice Industry',
 'This course covers the principles of food production and service in the restaurant and foodservice industry.'),
('Production Technology and Catering',
 'This course covers the principles of food production and service for catering and banquet events.'),

--* Electrical and heat power engineering department
-- Heat Power Engineering faculty
('Industrial Heat and Power Engineering',
 'This course covers the principles of heat and power engineering in industrial settings, including thermal cycles, heat transfer, and power generation.'),
-- Electrical Power Engineering faculty
('Electric Power Systems and Networks',
 'This course covers the principles of electric power systems and networks, including transmission, distribution, and renewable energy integration.'),
('Power Stations',
 'This course covers the principles of power station design and operation, including thermal and nuclear power stations.'),
('Relay protection and automation of electric power systems',
 'This course covers the principles of relay protection and automation in electric power systems.'),
('Electrical and electronic devices',
 'This course covers the principles of electrical and electronic devices, including transformers, motors, and generators.'),
('High-voltage electric power and electrical engineering',
 'This course covers the principles of high-voltage electric power and electrical engineering, including insulation, lightning protection, and surge protection.'),
('Power supply',
 'This course covers the principles of power supply design and operation, including AC/DC converters and power electronics.'),
('Electrical equipment and electrical facilities of enterprises, organizations and institutions',
 'This course covers the principles of electrical equipment and facilities in enterprise and institutional settings, including lighting, ventilation, and air conditioning.'),
-- Power Machine Engineering faculty
('Gas turbine, steam turbine installations and engines',
 'This course will cover the principles and design of gas turbines and steam turbines, as well as the installation and operation of these machines. Students will also learn about the various components of gas and steam turbine engines, such as compressors, combustion chambers, and turbines.'),
('Internal Combustion Engines',
 'This course will focus on the design and operation of internal combustion engines, including gasoline and diesel engines. Students will learn about engine components such as pistons, valves, and crankshafts, as well as fuel systems and emissions control.'),
('Hydraulic machines, hydraulic drives and hydropneumoautomatics',
 'This course will cover the principles and applications of hydraulic systems, including hydraulic machines such as pumps and motors, hydraulic drives, and hydropneumoautomatics. Students will also learn about the design and operation of hydraulic control systems.'),
('Compressor and refrigeration units of the fuel and energy complex',
 'This course will focus on the design and operation of compressor and refrigeration units used in the fuel and energy complex, such as in oil and gas processing. Students will learn about different types of compressors and refrigeration systems, as well as their applications and maintenance.'),
('Aircraft engines and power plants',
 'This course will cover the design and operation of aircraft engines and power plants, including both piston and turbine engines. Students will learn about engine components, fuel systems, and engine management systems, as well as aircraft propulsion and power generation. '),
('Gas turbine units of gas pumping stations',
 'The course covers the design, operation, maintenance, and optimization of gas turbine units used in gas pumping stations. Topics include thermodynamics of gas turbine cycles, gas turbine components, fuel selection, and environmental issues.'),
('Turbines and aircraft engines',
 'This course focuses on the design, operation, and maintenance of turbines and aircraft engines. Topics include gas turbine engines, internal combustion engines, compressor and turbine design, and materials used in turbine components.'),
-- Nuclear Power Engineering and Thermophysics faculty
('Nuclear Power Plants and Units',
 'Course that covers the principles of nuclear energy and the design, construction, and operation of nuclear power plants. The course includes an in-depth study of nuclear reactors, fuel cycles, and safety systems, as well as the environmental impact of nuclear energy. Students will learn about the different types of nuclear reactors, such as pressurized water reactors and boiling water reactors, and the associated systems for control and protection.');

INSERT INTO teacher_titles (name)
VALUES ('Professor'),
       ('Lecturer'),
       ('Assistant');


INSERT INTO users (user_type, first_name, last_name, gender, email, password, group_id, teacher_title_id, department_id)
VALUES ('student', 'Tom', 'Robinson', 0, 'tomrobinson@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Rory', 'McDonald', 0, 'mcdonald@yandex.ru',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Kate', 'Austin', 1, 'kitty@yahoo.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Amanda', 'Johnson', 1, 'amandaj@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Ron', 'Wisley', 0, 'ron@rambler.ru', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK',
        NULL, NULL, NULL),
       ('student', 'Harry', 'Portnoy', 0, 'gryffindor@hogwarts.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Lilly', 'Potter', 1, 'lilly@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Miguel', 'Fernandez', 0, 'fernandez@yahoo.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'John', 'Doe', 0, 'johndoe@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Jane', 'Doe', 1, 'janedoe@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Bob', 'Smith', 0, 'bobsmith@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Mary', 'Johnson', 1, 'maryjohnson@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'David', 'Lee', 0, 'davidlee@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Sarah', 'Kim', 1, 'sarahkim@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Michael', 'Wang', 0, 'michaelwang@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Emily', 'Chen', 1, 'emilychen@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'William', 'Liu', 0, 'williamliu@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Lucy', 'Garcia', 1, 'lucygarcia@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Andrew', 'Martinez', 0, 'andrewmartinez@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Olivia', 'Rodriguez', 1, 'oliviarodriguez@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Daniel', 'Lee', 0, 'daniellee@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Sophia', 'Hernandez', 1, 'sophiahernandez@example.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Emma', 'Jones', 1, 'emmajones@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Ethan', 'Jackson', 0, 'ethanjackson@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Mia', 'Clark', 1, 'miaclark@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Oliver', 'Lee', 0, 'oliverlee@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Ava', 'Green', 1, 'avagreen@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Noah', 'Baker', 0, 'noahbaker@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Charlotte', 'Garcia', 1, 'charlottegarcia@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Liam', 'Rodriguez', 0, 'liamrodriguez@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Amelia', 'Martinez', 1, 'ameliamartinez@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Lucas', 'Hernandez', 0, 'lucashernandez@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Emily', 'Lopez', 1, 'emilylopez@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Mason', 'Gonzalez', 0, 'masongonzalez@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Sofia', 'Perez', 1, 'sofiaperez@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Elijah', 'Sanchez', 0, 'elijahsanchez@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Victoria', 'Taylor', 1, 'victoriataylor@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Adam', 'Smith', 0, 'asmith@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Mia', 'Garcia', 1, 'miagarcia@hotmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Oliver', 'Davis', 0, 'odavis@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Isabella', 'Miller', 1, 'isamiller@yahoo.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Lucas', 'Martinez', 0, 'lucasmartinez@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Charlotte', 'Wilson', 1, 'cwilson@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Noah', 'Taylor', 0, 'ntaylor@yahoo.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL),
       ('student', 'Sophia', 'Anderson', 1, 'sanderson@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL);

INSERT INTO users (user_type, first_name, last_name, gender, email, password, group_id, teacher_title_id, department_id)
VALUES ('teacher', 'John', 'Smith', 0, 'johnsmith@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 1, NULL),
       ('teacher', 'Jane', 'Carlos', 1, 'carlos@yandex.ru',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 2, NULL),
       ('teacher', 'David', 'Johnson', 0, 'davidj@yahoo.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK#', NULL, 3, NULL),
       ('teacher', 'Sarah', 'Lee', 1, 'sarahlee@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 1, NULL),
       ('teacher', 'Michael', 'Jordan', 0, 'mjordan@rambler.ru',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 2, NULL),
       ('teacher', 'Katie', 'Johnson', 1, 'katiej@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 3, NULL),
       ('teacher', 'Anthony', 'Giddens', 0, 'giddensa@yahoo.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 1, NULL),
       ('teacher', 'Linda', 'Briskin', 1, 'lindab@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 2, NULL),
       ('teacher', 'James', 'Baldwin', 0, 'baldwinj@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 3, NULL),
       ('teacher', 'Angela', 'Davis', 1, 'davisa@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK#', NULL, 1, NULL),
       ('teacher', 'Robert', 'Sapolski', 0, 'rsapolski@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 1, NULL),
       ('teacher', 'Jordan', 'Pitersen', 0, 'pitersen@mail.ru',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK#$', NULL, 2, NULL),
       ('teacher', 'Kristina', 'Drugova', 1, 'drugova@gmail.com',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 2, NULL),
       ('teacher', 'admin', 'admin', 0, 'admin',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 1, NULL),
       ('teacher', 'teacher', 'teacher', 1, 'teacher',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, 1, NULL),
       ('student', 'student', 'student', 1, 'student',
        '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', NULL, NULL, NULL);

INSERT INTO groups (name, faculty_id, head_user_id, education_form_id)
VALUES ('g1', 1, 1, 1),
       ('g2', 1, 6, 2),
       ('g3', 2, 11, 2),
       ('g4', 2, 16, 1),
       ('g5', 3, 21, 1),
       ('g6', 3, 26, 2),
       ('g7', 4, 31, 2),
       ('g8', 4, 36, 1),
       ('g9', 5, 41, 1);

UPDATE users SET group_id = 1 WHERE id = 1;
UPDATE users SET group_id = 1 WHERE id = 2;
UPDATE users SET group_id = 1 WHERE id = 3;
UPDATE users SET group_id = 1 WHERE id = 4;
UPDATE users SET group_id = 1 WHERE id = 5;

UPDATE users SET group_id = 2 WHERE id = 6;
UPDATE users SET group_id = 2 WHERE id = 7;
UPDATE users SET group_id = 2 WHERE id = 8;
UPDATE users SET group_id = 2 WHERE id = 9;
UPDATE users SET group_id = 2 WHERE id = 10;

UPDATE users SET group_id = 3 WHERE id = 11;
UPDATE users SET group_id = 3 WHERE id = 12;
UPDATE users SET group_id = 3 WHERE id = 13;
UPDATE users SET group_id = 3 WHERE id = 14;
UPDATE users SET group_id = 3 WHERE id = 15;

UPDATE users SET group_id = 4 WHERE id = 16;
UPDATE users SET group_id = 4 WHERE id = 17;
UPDATE users SET group_id = 4 WHERE id = 18;
UPDATE users SET group_id = 4 WHERE id = 19;
UPDATE users SET group_id = 4 WHERE id = 20;

UPDATE users SET group_id = 5 WHERE id = 21;
UPDATE users SET group_id = 5 WHERE id = 22;
UPDATE users SET group_id = 5 WHERE id = 23;
UPDATE users SET group_id = 5 WHERE id = 24;
UPDATE users SET group_id = 5 WHERE id = 25;

UPDATE users SET group_id = 6 WHERE id = 26;
UPDATE users SET group_id = 6 WHERE id = 27;
UPDATE users SET group_id = 6 WHERE id = 28;
UPDATE users SET group_id = 6 WHERE id = 29;
UPDATE users SET group_id = 6 WHERE id = 30;

UPDATE users SET group_id = 7 WHERE id = 31;
UPDATE users SET group_id = 7 WHERE id = 32;
UPDATE users SET group_id = 7 WHERE id = 33;
UPDATE users SET group_id = 7 WHERE id = 34;
UPDATE users SET group_id = 7 WHERE id = 35;

UPDATE users SET group_id = 8 WHERE id = 36;
UPDATE users SET group_id = 8 WHERE id = 37;
UPDATE users SET group_id = 8 WHERE id = 38;
UPDATE users SET group_id = 8 WHERE id = 39;
UPDATE users SET group_id = 8 WHERE id = 40;

UPDATE users SET group_id = 9 WHERE id = 41;
UPDATE users SET group_id = 9 WHERE id = 42;
UPDATE users SET group_id = 9 WHERE id = 43;
UPDATE users SET group_id = 9 WHERE id = 44;
UPDATE users SET group_id = 9 WHERE id = 45;

INSERT INTO user_groups
VALUES (46, 1), (46, 2), (46, 3), (46, 4), (47, 1), (47, 2), (47, 3), (47, 4), (48, 5), (48, 6), (48, 7), (48, 8),
       (49, 5), (49, 6), (49, 7), (49, 8), (50, 9), (50, 2), (50, 3), (50, 4), (51, 9), (51, 2), (51, 3), (51, 4),
       (52, 5), (52, 6), (52, 7), (52, 8), (53, 5), (53, 6), (53, 7), (53, 8), (54, 1), (54, 2), (54, 3), (54, 4),
       (55, 1), (55, 2), (55, 3), (55, 9), (56, 5), (56, 6), (56, 7), (56, 8), (57, 5), (57, 9), (57, 7), (57, 8),
       (58, 3), (58, 4), (58, 9), (58, 6);

INSERT INTO user_courses
VALUES (46, 1), (46, 2), (46, 3), (46, 5), (46, 6), (46, 7), (47, 1), (47, 2), (47, 3), (47, 4), (47, 6), (47, 7),
       (48, 8), (48, 9), (49, 10), (49, 11), (49, 12), (49, 13), (49, 14), (50, 10), (50, 11), (50, 12), (50, 13),
       (50, 14), (51, 15), (51, 18), (51, 19), (52, 15), (52, 16), (52, 17), (52, 18), (53, 20), (53, 21), (53, 22),
       (53, 23), (53, 24), (53, 25), (53, 26), (53, 27), (53, 28), (54, 35), (54, 29), (54, 30), (54, 31), (54, 32),
       (54, 33), (54, 34), (55, 1), (55, 2), (55, 3), (55, 5), (55, 6), (55, 7), (56, 8), (56, 9), (57, 10), (57, 11),
       (57, 13), (57, 14), (58, 20), (58, 21), (58, 22), (58, 23), (58, 24), (58, 25), (58, 26), (58, 27), (58, 28),
       (58, 29), (58, 30), (58, 31), (58, 32), (58, 33), (58, 34);

INSERT INTO departments (name, description)
VALUES ('Mathematics and mechanics',
        'This department focuses on the study of mathematical concepts and their applications to physical systems. This includes topics such as calculus, differential equations, linear algebra, and mechanics. Students in this department may learn how to model and analyze the behavior of objects and systems using mathematical techniques.'),
       ('Computer and information sciences',
        'This department deals with the study of computing and information systems, including both hardware and software. Topics may include programming, algorithms, databases, computer networks, cybersecurity, and artificial intelligence. Students in this department may learn how to design and implement software, manage data, and solve complex problems using computational methods.'),
       ('Physics and astronomy',
        'This department focuses on the study of the physical universe, including matter, energy, and the laws that govern their interactions. Topics may include classical mechanics, electromagnetism, thermodynamics, quantum mechanics, astrophysics, and cosmology. Students in this department may learn how to use mathematical and experimental methods to understand the behavior of the universe at all scales.'),
       ('Industrial ecology and biotechnology',
        'This department deals with the study of the interactions between human society and the natural environment. Topics may include sustainability, environmental science, ecology, biotechnology, and green technology. Students in this department may learn how to design and implement solutions to environmental problems using a systems-based approach.'),
       ('Electrical and heat power engineering',
        'This department focuses on the study of electrical and thermal systems and their applications to engineering problems. Topics may include circuit design, power electronics, control systems, renewable energy, and thermodynamics. Students in this department may learn how to design and analyze systems that convert electrical or thermal energy into useful work or other forms of energy. ');

UPDATE users SET department_id = 1 WHERE id = 46;
UPDATE users SET department_id = 1 WHERE id = 47;
UPDATE users SET department_id = 2 WHERE id = 48;
UPDATE users SET department_id = 3 WHERE id = 49;
UPDATE users SET department_id = 3 WHERE id = 50;
UPDATE users SET department_id = 4 WHERE id = 51;
UPDATE users SET department_id = 4 WHERE id = 52;
UPDATE users SET department_id = 5 WHERE id = 53;
UPDATE users SET department_id = 5 WHERE id = 54;
UPDATE users SET department_id = 1 WHERE id = 55;
UPDATE users SET department_id = 2 WHERE id = 56;
UPDATE users SET department_id = 3 WHERE id = 57;
UPDATE users SET department_id = 5 WHERE id = 58;

INSERT INTO department_courses
VALUES
--Mathematics and mechanics department
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
-- Computer and information sciences department
(2, 8), (2, 9),
-- Physics and astronomy department
(3, 10), (3, 11), (3, 12), (3, 13), (3, 14),
-- Industrial ecology and biotechnology department
(4, 15), (4, 16), (4, 17), (4, 18), (4, 19),
-- Electrical and heat power engineering department
(5, 20), (5, 21), (5, 22), (5, 23), (5, 24), (5, 25), (5, 26), (5, 27), (5, 28), (5, 29), (5, 30), (5, 31), (5, 32),
(5, 33), (5, 34), (5, 35);

INSERT INTO lessons (name, group_id, user_id, course_id, lesson_type_id, start_time)
VALUES ('lesson 1', 1, 46, 1, 1, '2023-06-22 12:00:00'),
       ('lesson 2', 2, 47, 2, 2, '2023-06-22 12:00:00'),
       ('lesson 3', 3, 48, 3, 3, '2023-06-22 12:00:00'),
       ('lesson 4', 4, 49, 4, 4, '2023-06-22 12:00:00'),
       ('lesson 5', 5, 50, 5, 5, '2023-06-22 12:00:00'),
       ('lesson 6', 6, 51, 6, 1, '2023-06-22 12:00:00'),
       ('lesson 7', 7, 52, 7, 2, '2023-06-22 12:00:00'),
       ('lesson 8', 8, 53, 8, 3, '2023-06-22 16:00:00'),
       ('lesson 9', 9, 54, 9, 4, '2023-06-22 16:00:00'),
       ('lesson 10', 1, 55, 10, 5, '2023-06-22 16:00:00'),
       ('lesson 11', 2, 56, 11, 1, '2023-06-22 16:00:00'),
       ('lesson 12', 3, 57, 12, 2, '2023-06-22 16:00:00'),
       ('lesson 13', 4, 58, 13, 3, '2023-06-22 16:00:00'),
       ('lesson 14', 1, 46, 14, 4, '2023-06-23 10:00:00'),
       ('lesson 15', 2, 47, 15, 5, '2023-06-23 10:00:00'),
       ('lesson 16', 3, 48, 16, 1, '2023-06-23 10:00:00'),
       ('lesson 17', 4, 49, 17, 2, '2023-06-23 14:00:00'),
       ('lesson 18', 5, 50, 18, 3, '2023-06-23 14:00:00'),
       ('lesson 19', 6, 51, 19, 4, '2023-06-23 14:00:00'),
       ('lesson 20', 7, 52, 20, 5, '2023-06-23 18:00:00'),
       ('lesson 21', 8, 53, 21, 1, '2023-06-23 18:00:00'),
       ('lesson 22', 9, 54, 22, 2, '2023-06-23 18:00:00'),
       ('lesson 23', 1, 55, 23, 3, '2023-06-24 12:00:00'),
       ('lesson 24', 2, 56, 24, 4, '2023-06-24 12:00:00'),
       ('lesson 25', 3, 57, 25, 5, '2023-06-24 12:00:00'),
       ('lesson 26', 4, 58, 26, 1, '2023-06-24 16:00:00'),
       ('lesson 27', 5, 46, 27, 2, '2023-06-24 16:00:00'),
       ('lesson 28', 6, 47, 28, 3, '2023-06-24 16:00:00'),
       ('lesson 29', 7, 48, 29, 4, '2023-06-24 16:00:00'),
       ('lesson 30', 8, 49, 30, 5, '2023-06-24 16:00:00'),
       ('lesson 31', 9, 50, 31, 1, '2023-06-25 10:00:00'),
       ('lesson 32', 1, 51, 32, 2, '2023-06-25 10:00:00'),
       ('lesson 33', 2, 52, 33, 3, '2023-06-25 10:00:00'),
       ('lesson 34', 3, 53, 34, 4, '2023-06-25 14:00:00'),
       ('lesson 35', 4, 54, 35, 5, '2023-06-25 14:00:00'),
       ('lesson 36', 5, 55, 1, 1, '2023-06-25 14:00:00'),
       ('lesson 37', 6, 56, 2, 2, '2023-06-25 18:00:00'),
       ('lesson 38', 7, 57, 3, 3, '2023-06-25 18:00:00'),
       ('lesson 39', 8, 58, 4, 4, '2023-06-25 18:00:00'),
       ('lesson 40', 9, 46, 5, 5, '2023-06-26 12:00:00'),
       ('lesson 41', 1, 47, 6, 1, '2023-06-26 12:00:00'),
       ('lesson 42', 2, 48, 7, 2, '2023-06-26 12:00:00'),
       ('lesson 43', 3, 49, 8, 3, '2023-06-26 12:00:00'),
       ('lesson 44', 4, 50, 9, 4, '2023-06-26 16:00:00'),
       ('lesson 45', 5, 51, 10, 5, '2023-06-26 16:00:00'),
       ('lesson 46', 6, 51, 26, 1, '2023-07-04 12:00:00'),
       ('lesson 47', 7, 52, 27, 2, '2023-07-04 12:00:00'),
       ('lesson 48', 8, 53, 28, 3, '2023-07-04 16:00:00'),
       ('lesson 49', 9, 54, 29, 4, '2023-07-04 16:00:00'),
       ('lesson 50', 1, 55, 30, 5, '2023-07-04 16:00:00'),
       ('lesson 51', 2, 56, 31, 1, '2023-07-04 16:00:00'),
       ('lesson 52', 3, 57, 32, 2, '2023-07-05 10:00:00'),
       ('lesson 53', 4, 58, 33, 3, '2023-07-05 10:00:00'),
       ('lesson 54', 5, 46, 34, 4, '2023-07-05 10:00:00'),
       ('lesson 55', 6, 47, 35, 5, '2023-07-05 14:00:00');

INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_TEACHER'),
       ('ROLE_STUDENT');

INSERT INTO privileges (name)
VALUES ('READ'),
       ('WRITE'),
       ('DELETE');

INSERT INTO role_privileges (role_id, privilege_id)
VALUES (1, 1), (1, 2), (1, 3), (2, 1), (2, 2), (3, 1);

INSERT INTO user_roles (user_id, role_id)
SELECT id, 2
FROM users
WHERE user_type = 'teacher';

INSERT INTO user_roles (user_id, role_id)
SELECT id, 3
FROM users
WHERE user_type = 'student';

-- Set ROLE_ADMIN for admin user
UPDATE user_roles SET role_id = 1 WHERE user_id = 59;