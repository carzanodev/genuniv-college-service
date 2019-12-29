CREATE TABLE IF NOT EXISTS college
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR NOT NULL,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS degree_type
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR NOT NULL,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS degree
(
    id             SERIAL PRIMARY KEY,
    code           VARCHAR NOT NULL,
    name           VARCHAR NOT NULL,
    degree_type_id INTEGER NOT NULL REFERENCES degree_type,
    college_id     INTEGER NOT NULL REFERENCES college,
    is_active      BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS classroom
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR NOT NULL,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS course
(
    id            SERIAL PRIMARY KEY,
    code          VARCHAR NOT NULL,
    name          VARCHAR NOT NULL,
    description   VARCHAR,
    unit          DECIMAL          DEFAULT 0,
    lecture_hours INTEGER          DEFAULT 0,
    lab_hours     INTEGER          DEFAULT 0,
    college_id    INTEGER NOT NULL REFERENCES college,
    is_active     BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at   TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS offering
(
    id               SERIAL PRIMARY KEY,
    course_id        INTEGER NOT NULL REFERENCES course,
    schedule_id      INTEGER NOT NULL,
    faculty_id       INTEGER          DEFAULT NULL,
    classroom_id     INTEGER          DEFAULT NULL,
    capacity         INTEGER          DEFAULT 0,
    school_period_id INTEGER NOT NULL,
    school_year_id   INTEGER NOT NULL,
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION updated_at_now()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON college
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();
CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON degree_type
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();
CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON degree
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();
CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON classroom
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();
CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON course
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();
CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON offering
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();