package com.ead.authuser.dtos;

import com.ead.authuser.validations.PasswordConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserDto(

        @NotBlank(groups = UserView.RegistrationPost.class, message = "Username cannot be blank")
        @Length(groups = UserView.RegistrationPost.class, min = 4, max = 50, message = "Username must be between 4 " +
                "and 50 characters")
        @JsonView(UserView.RegistrationPost.class)
        String username,

        @Email(groups = UserView.RegistrationPost.class, message = "Email should be valid")
        @Length(groups = UserView.RegistrationPost.class, max = 100, message = "Email must be at most 100 characters")
        @NotBlank(groups = UserView.RegistrationPost.class, message = "Email cannot be blank")
        @JsonView(UserView.RegistrationPost.class)
        String email,

        @PasswordConstraint(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
        @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class}, message = "Password cannot " +
                "be blank")
        @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
        String password,

        @PasswordConstraint(groups = UserView.PasswordPut.class)
        @NotBlank(groups = UserView.PasswordPut.class, message = "Old password cannot be blank")
        @JsonView(UserView.PasswordPut.class)
        String oldPassword,

        @NotBlank(groups = {UserView.RegistrationPost.class, UserView.UserPut.class}, message = "Full name cannot be " +
                "blank")
        @Length(groups = {UserView.RegistrationPost.class, UserView.UserPut.class}, max = 150, message = "Full name " +
                "must be at most 150 characters")
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String fullName,

        @NotBlank(groups = {UserView.RegistrationPost.class, UserView.UserPut.class}, message = "Phone number cannot " +
                "be blank")
        @Length(groups = {UserView.RegistrationPost.class, UserView.UserPut.class}, max = 20, message = "Phone number" +
                " must be at most 20 characters")
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String phoneNumber

) {
    public interface UserView {
        interface RegistrationPost {
        }

        interface UserPut {
        }

        interface PasswordPut {
        }
    }
}
