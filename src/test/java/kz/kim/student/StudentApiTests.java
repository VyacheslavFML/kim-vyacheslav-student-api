package kz.kim.student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.*;
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StudentApiTests {
 @Autowired TestRestTemplate http;
 @Autowired StudentRepository repository;
 @BeforeEach void clean(){repository.deleteAll();}
 StudentRequest valid(){return new StudentRequest("Ким Вячеслав Александрович","kim.vyacheslav@example.com",21);}
 @Test void personalizedHome(){assertThat(http.getForObject("/",String.class)).contains("Ким Вячеслав Александрович");}
 @Test void createReadDelete(){
  var created=http.postForEntity("/api/students",valid(),Student.class);
  assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  assertThat(created.getHeaders().getLocation()).isNotNull();
  var url=created.getHeaders().getLocation().toString();
  assertThat(http.getForObject(url,String.class)).contains("kim.vyacheslav@example.com");
  assertThat(http.getForObject("/api/students",String.class)).contains("Ким Вячеслав");
  assertThat(http.exchange(url,HttpMethod.DELETE,HttpEntity.EMPTY,Void.class).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  assertThat(http.getForEntity(url,String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
 }
 @Test void missingIs404(){assertThat(http.getForEntity("/api/students/999999",String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);}
 @Test void invalidIs400(){assertThat(http.postForEntity("/api/students",new StudentRequest("","bad",0),String.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);}
 @Test void duplicateIs409(){http.postForEntity("/api/students",valid(),String.class);assertThat(http.postForEntity("/api/students",valid(),String.class).getStatusCode()).isEqualTo(HttpStatus.CONFLICT);}
 @Test void controlledFailure(){assertThat(Boolean.getBoolean("controlledFailure")).as("Deliberate CI failure only with -PcontrolledFailure=true").isFalse();}
}
