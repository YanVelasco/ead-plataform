-- Create TB_USERS_COURSES table
CREATE TABLE IF NOT EXISTS TB_USERS_COURSES
(
    user_course_id UUID PRIMARY KEY,
    course_id UUID NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_tb_users_courses_user_id
        FOREIGN KEY (user_id) REFERENCES TB_USERS (user_id)
            ON DELETE CASCADE
);
