package kz.kim.student;
import jakarta.persistence.*;
@Entity
@Table(name="students", uniqueConstraints=@UniqueConstraint(name="uk_student_email", columnNames="email"))
public class Student {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false, length=120) private String name;
 @Column(nullable=false, length=254) private String email;
 @Column(nullable=false) private Integer age;
 protected Student() {}
 public Student(String name,String email,Integer age) { set(name,email,age); }
 public void set(String name,String email,Integer age) { this.name=name; this.email=email; this.age=age; }
 public Long getId(){return id;}
 public String getName(){return name;}
 public String getEmail(){return email;}
 public Integer getAge(){return age;}
}
