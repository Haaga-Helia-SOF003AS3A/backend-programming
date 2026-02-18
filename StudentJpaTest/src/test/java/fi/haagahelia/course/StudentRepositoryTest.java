package fi.haagahelia.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import fi.haagahelia.course.domain.Department;
import fi.haagahelia.course.domain.DepartmentRepository;
import fi.haagahelia.course.domain.Student;
import fi.haagahelia.course.domain.StudentRepository;


//@DataJpaTest //if you are using in-memory database, like H2
@SpringBootTest(classes = StudentListApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //if you are using real db
public class StudentRepositoryTest {

    //in test cases field injection can be used
    @Autowired
    private StudentRepository repository;

    @Autowired
    private DepartmentRepository drepository;
    
    @Test
    public void findByLastnameShouldReturnStudent() {
        List<Student> students = repository.findByLastName("Johnson");
        
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getFirstName()).isEqualTo("John");
    }
    
    @Test
    public void createNewStudent() {
    	Department department = new Department("BITE");
    	drepository.save(department);
    	Student student = new Student("Mickey", "Mouse", "mm@mouse.com", department);
    	repository.save(student);
    	assertThat(student.getId()).isNotNull();
    }    
    @Test
    public void deleteNewStudent() {
		List<Student> students = repository.findByLastName("Johnson");
		Student student = students.get(0);
		repository.delete(student);
		List<Student> newStudents = repository.findByLastName("Johnson");
		assertThat(newStudents).hasSize(0);
     }

}
