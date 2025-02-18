package org.emocare.emocare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

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

    @NotBlank(message = "First Name is mandatory")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @Past(message = "DateOfBirth must be in the past")
    private Date dateOfBirth;
}
