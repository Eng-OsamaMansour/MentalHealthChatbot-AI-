package org.emocare.emocare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import org.emocare.emocare.model.Role;

import java.util.Date;

@Data
@Builder
public class UserDto
{
    private String id;

    @NotBlank(message = "User Name is mandatory")
    private String username;

    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String email;

    @NotBlank(message = "Password is mandatory")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    private String address;

    private String city;

    private Role role;
}
