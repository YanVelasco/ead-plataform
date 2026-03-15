CREATE TABLE `tb_courses_users`
(
    `course_id` BINARY(16) NOT NULL,
    `user_id`   BINARY(16) NOT NULL,
    PRIMARY KEY (`course_id`, `user_id`),
    CONSTRAINT `fk_courses_users_course` FOREIGN KEY (`course_id`) REFERENCES `tb_courses` (`course_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_courses_users_user` FOREIGN KEY (`user_id`) REFERENCES `tb_users` (`user_id`) ON DELETE CASCADE,
    INDEX `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

