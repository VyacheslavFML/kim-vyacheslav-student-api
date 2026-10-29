package kz.kim.student;
import jakarta.validation.constraints.*;
public record StudentRequest(@NotBlank @Size(max=120) String name,
 @NotBlank @Email @Size(max=254) String email, @NotNull @Min(1) @Max(120) Integer age) {}
