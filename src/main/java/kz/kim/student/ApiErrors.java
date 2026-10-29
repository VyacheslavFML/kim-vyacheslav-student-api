package kz.kim.student;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
@RestControllerAdvice
public class ApiErrors {
 @ExceptionHandler(MethodArgumentNotValidException.class)
 ResponseEntity<ProblemDetail> invalid(MethodArgumentNotValidException e){
  return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Name, email and age must be valid"));
 }
 @ExceptionHandler(DataIntegrityViolationException.class)
 ResponseEntity<ProblemDetail> duplicate(DataIntegrityViolationException e){
  return ResponseEntity.status(409).body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,"Student email must be unique"));
 }
}
