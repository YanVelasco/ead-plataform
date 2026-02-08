CREATE TABLE `tb_lessons`
(
    `lesson_id`     BINARY(16)   NOT NULL,
    `title`         VARCHAR(150) NOT NULL,
    `description`   LONGTEXT     NOT NULL,
    `video_url`     VARCHAR(500) NOT NULL,
    `creation_date` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `module_id`     BINARY(16)   NOT NULL,
    PRIMARY KEY (`lesson_id`),
    KEY `fk_lessons_module` (`module_id`),
    CONSTRAINT `fk_lessons_module` FOREIGN KEY (`module_id`) REFERENCES `tb_modules` (`module_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

