CREATE TABLE `tb_courses_users`
(
    `course_user_id` BINARY(16) NOT NULL,
    `user_id`        BINARY(16) NOT NULL,
    `course_id`      BINARY(16) NOT NULL,
    PRIMARY KEY (`course_user_id`),
    KEY `fk_courses_users_course` (`course_id`),
    KEY `idx_courses_users_user_id` (`user_id`),
    CONSTRAINT `fk_courses_users_course` FOREIGN KEY (`course_id`) REFERENCES `tb_courses` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
