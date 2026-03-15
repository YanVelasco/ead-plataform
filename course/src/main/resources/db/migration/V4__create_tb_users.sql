CREATE TABLE `tb_users`
(
    `user_id`     BINARY(16)   NOT NULL,
    `email`       VARCHAR(50)  NOT NULL,
    `full_name`   VARCHAR(255) NOT NULL,
    `user_status` VARCHAR(20)  NOT NULL,
    `user_type`   VARCHAR(20)  NOT NULL,
    `image_url`   VARCHAR(255),
    PRIMARY KEY (`user_id`),
    UNIQUE INDEX `idx_email` (`email`),
    INDEX `idx_user_status` (`user_status`),
    INDEX `idx_user_type` (`user_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

