package fi.haagahelia.course.web;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import fi.haagahelia.course.domain.DepartmentRepository;
import fi.haagahelia.course.domain.Student;
import fi.haagahelia.course.domain.StudentRepository;

@Controller
public class StudentController {
 
    // https://docs.spring.io/spring-boot/reference/using/spring-beans-and-dependency-injection.html
 	private final StudentRepository studentRepository; 

	private final DepartmentRepository departmentRepository; 
	
	// Constructor Injection
	public StudentController(StudentRepository studentRepository, 
			DepartmentRepository departmentRepository) {
		this.studentRepository = studentRepository; 
		this.departmentRepository = departmentRepository;
	}	
	
	// Show all students
    @RequestMapping(value="/studentlist")
    public String studentList(Model model) {
  	
        model.addAttribute("students", studentRepository.findAll());
        return "studentlist";
    }
  
    // Add new student
    @RequestMapping(value = "/add")
    public String addStudent(Model model){
    	model.addAttribute("student", new Student());
    	model.addAttribute("departments", departmentRepository.findAll());
        return "addstudent";
    }     
    
    // Save new student
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Student student){
        studentRepository.save(student);
        return "redirect:studentlist";
    }    

    // Delete student
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteStudent(@PathVariable("id") Long studentId, Model model) {
    	studentRepository.deleteById(studentId);
        return "redirect:../studentlist";
    }     
}
