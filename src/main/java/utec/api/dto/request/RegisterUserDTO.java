package utec.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterUserDTO {

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-Z].*", message = "First name must start with uppercase letter")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-Z].*", message = "Last name must start with uppercase letter")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must be at least 8 characters with at least one letter and one number")
    private String password;
}

