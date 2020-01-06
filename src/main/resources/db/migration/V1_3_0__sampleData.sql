INSERT INTO degree (code, name, degree_type_id, college_id)
VALUES ('BSEcon', 'Bachelor of Science in Economics', 1, 1),
       ('BSPsych', 'Bachelor of Science in Psychology', 1, 1),
       ('BSA', 'Bachelor of Science in Accountancy', 1, 2),
       ('BSBA', 'Bachelor of Science in Business Administration', 1, 2),
       ('BSEE', 'Bachelor of Science in Electrical Engineering', 1, 3),
       ('BSME', 'Bachelor of Science in Mechanical Engineering', 1, 3),
       ('BSECE', 'Bachelor of Science in Electronics Engineering', 1, 3),
       ('BSComE', 'Bachelor of Science in Computer Engineering', 1, 3),
       ('BSEd - SE', 'Bachelor of Science in Secondary Education', 1, 4),
       ('BSN', 'Bachelor of Science in Nursing', 1, 5),
       ('BSIT', 'Bachelor of Science in Information Technology', 1, 6);

INSERT INTO course (code, name, description, unit, lecture_hours, lab_hours, college_id)
VALUES ('ENG101', 'Basic English', 'Intro to English', 3, 3, 0, 1),
       ('BUS101', 'Basic Business', 'Intro to Business', 3, 3, 0, 2),
       ('ENGG101', 'General Engineering', 'Intro to Engineering', 3, 3, 0, 3);

INSERT INTO classroom (name)
VALUES ('R100'),
       ('R101'),
       ('R102'),
       ('R103'),
       ('R104'),
       ('R105'),
       ('R201'),
       ('R202'),
       ('R203');

INSERT INTO offering (course_id, schedule_id, faculty_id, classroom_id, capacity, school_period_id, school_year_id)
VALUES (1, 1, 1, 1, 45, 1, 1),
       (2, 2, 2, 2, 40, 1, 1),
       (3, 3, 3, 3, 35, 1, 1);