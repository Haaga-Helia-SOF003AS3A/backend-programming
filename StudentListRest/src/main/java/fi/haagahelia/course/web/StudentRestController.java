package fi.haagahelia.course.web;


import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.course.domain.Student;
import fi.haagahelia.course.domain.StudentRepository;

@RestController
public class StudentRestController {

	private final StudentRepository studentRepository; 
	// Constructor Injection 
	public StudentRestController(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}
		
	  
	@GetMapping("/students")
    public Iterable<Student> findAllStudents() {
        return studentRepository.findAll();
    }
    
    @GetMapping("/students/{id}")
    public Optional<Student> findById(@PathVariable("id") Long studentId) {
        return studentRepository.findById(studentId);
    }

    @PostMapping("/students")
    public Student saveStudent(@RequestBody Student student) {
		System.out.println("saveStudent " + student);
        return studentRepository.save(student);
    }

    @PutMapping("students/{id}")
    public Student saveEditedStudent(@RequestBody Student editedStudent, @PathVariable Long id) {
		editedStudent.setId(id);
		return studentRepository.save(editedStudent);
	}
    @DeleteMapping("/students/{id}")
    // public void deleteStudent(@PathVariable Long id) {
    public Iterable<Student> deleteStudent(@PathVariable Long id) {
        
        studentRepository.deleteById(id);
        return studentRepository.findAll();
    }


	    
	  
}
