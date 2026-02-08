CREATE TABLE `tb_modules`
(
    `module_id`     BINARY(16)   NOT NULL,
    `title`         VARCHAR(150) NOT NULL,
    `description`   LONGTEXT     NOT NULL,
    `creation_date` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `course_id`     BINARY(16)   NOT NULL,
    PRIMARY KEY (`module_id`),
    KEY `fk_modules_course` (`course_id`),
    CONSTRAINT `fk_modules_course` FOREIGN KEY (`course_id`) REFERENCES `tb_courses` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

