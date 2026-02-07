package com.ead.course.models;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_COURSES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_id")
    private UUID courseId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_update_date", nullable = false)
    private LocalDateTime lastUpdateDate;

    @Column(name = "course_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    @Column(name = "course_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @Column(name = "user_instructor", nullable = false)
    private UUID userInstructor;

    @Column(name = "image_url")
    private String imageUrl;

}
