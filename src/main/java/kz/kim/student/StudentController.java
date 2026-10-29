package kz.kim.student;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Sort;
import java.net.URI;
import java.util.*;
@RestController
public class StudentController {
 private final StudentRepository repository;
 public StudentController(StudentRepository repository){this.repository=repository;}
 @GetMapping("/") public Map<String,String> info(){return Map.of("application","kim-vyacheslav-student-api","student","Ким Вячеслав Александрович");}
 @GetMapping("/api/students") public List<Student> all(){return repository.findAll(Sort.by("id"));}
 @GetMapping("/api/students/{id}") public Student one(@PathVariable Long id){return find(id);}
 @PostMapping("/api/students") public ResponseEntity<Student> create(@Valid @RequestBody StudentRequest r){
  Student s=repository.saveAndFlush(new Student(r.name(),r.email(),r.age()));
  return ResponseEntity.created(URI.create("/api/students/"+s.getId())).body(s);
 }
 @DeleteMapping("/api/students/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){repository.delete(find(id));return ResponseEntity.noContent().build();}
 private Student find(Long id){return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found"));}
}
