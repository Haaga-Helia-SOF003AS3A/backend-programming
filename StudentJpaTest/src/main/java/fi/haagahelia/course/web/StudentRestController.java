package fi.haagahelia.course.web;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.course.domain.Student;
import fi.haagahelia.course.domain.StudentRepository;

@CrossOrigin
@Controller
public class StudentRestController {

    private StudentRepository studentRepository; 

    public StudentRestController(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    // RESTful service to get all students
    @RequestMapping(value = "/students")
    public @ResponseBody List<Student> studentListRest() {
        return (List<Student>) studentRepository.findAll();
    }

    // RESTful service to get student by id
    @RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Student> findStudentRest(@PathVariable("id") Long studentId) {
        return studentRepository.findById(studentId);
    }

}
