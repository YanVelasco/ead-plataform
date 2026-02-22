package com.ead.authuser.model;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_USERS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    UUID userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    String password;

    @Column(name = "full_name", nullable = false, length = 150)
    String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    UserType userType;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @Column(name = "image_url")
    @JdbcTypeCode(SqlTypes.VARBINARY)
    byte[] imageUrl;


    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "creation_date", nullable = false)
    LocalDateTime creationDate;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "last_update_date", nullable = false)
    LocalDateTime lastUpdateDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UserCourseModel> userCourseModels;

}
