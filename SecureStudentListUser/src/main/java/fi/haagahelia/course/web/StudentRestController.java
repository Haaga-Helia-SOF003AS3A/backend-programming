package fi.haagahelia.course.web;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.course.domain.DepartmentRepository;
import fi.haagahelia.course.domain.Student;
import fi.haagahelia.course.domain.StudentRepository;

@Controller 
public class StudentRestController {

    // https://docs.spring.io/spring-boot/reference/using/spring-beans-and-dependency-injection.html
 	private final StudentRepository studentRepository; 


	// Constructor Injection
	public StudentRestController(StudentRepository studentRepository, 
			DepartmentRepository departmentRepository) {
		this.studentRepository = studentRepository; 
	}
	
	// RESTful service to get all students
    @RequestMapping(value="/students")
    public @ResponseBody List<Student> studentListRest() {	
        return (List<Student>) studentRepository.findAll();
    }    

	// RESTful service to get student by id
    @RequestMapping(value="/student/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Student> findStudentRest(@PathVariable("id") Long studentId) {	
    	return studentRepository.findById(studentId);
    }  
    
    // RESTful service to save a new student 
    @PostMapping("/students")
    public @ResponseBody Student saveStudent(@RequestBody Student student) {	
    	return studentRepository.save(student);
    } 
    
    
}
