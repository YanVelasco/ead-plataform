CREATE TABLE `tb_courses` (
  `course_id` BINARY(16) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `creation_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `course_status` VARCHAR(20) NOT NULL,
  `course_level` VARCHAR(20) NOT NULL,
  `user_instructor` BINARY(16) NOT NULL,
  `image_url` VARCHAR(255),
  PRIMARY KEY (`course_id`),
  INDEX `idx_user_instructor` (`user_instructor`),
  INDEX `idx_course_status` (`course_status`),
  CONSTRAINT `chk_course_status` CHECK (`course_status` IN ('INPROGRESS', 'CONCLUDED')),
  CONSTRAINT `chk_course_level` CHECK (`course_level` IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

