package fi.haagahelia.course.web;


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
        return "redirect:/studentlist";
    }    

    // Delete student
    // Demo here how to use @PreAuthorize annotation
    //@PreAuthorize("hasAuthority('ADMIN')") 
    // a) try to delete student  (with USER ROLE)
    // b) @PreAuthorize delete only for ADMIN ROLE
    // c) try to delete student again  (with USER ROLE)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteStudent(@PathVariable("id") Long studentId, Model model) {
    	studentRepository.deleteById(studentId);
        return "redirect:../studentlist";
    }     
}
