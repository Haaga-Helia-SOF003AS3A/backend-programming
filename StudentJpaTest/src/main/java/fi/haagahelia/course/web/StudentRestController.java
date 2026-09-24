package fi.haagahelia.course.web;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.course.domain.Student;
import fi.haagahelia.course.domain.StudentRepository;

@RequestMapping("/students")
@CrossOrigin
@RestController
public class StudentRestController {

    // https://docs.spring.io/spring-boot/reference/using/spring-beans-and-dependency-injection.html
    private final StudentRepository studentRepository;

    // Constructor Injection
    public StudentRestController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;

    }

    // RESTful service to get all students
    @RequestMapping
    public List<Student> studentListRest() {
        return (List<Student>) studentRepository.findAll();
    }

    // RESTful service to get student by id
    @GetMapping(value = "/{id}")
    public Optional<Student> findStudentRest(@PathVariable("id") Long studentId) {
        return studentRepository.findById(studentId);
    }

}
