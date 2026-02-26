package fi.haagahelia.course.web;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.course.domain.DepartmentRepository;
import fi.haagahelia.course.domain.Student;
import fi.haagahelia.course.domain.StudentRepository;

@Controller
public class StudentController {

    // @Autowired
    private StudentRepository studentRepository;

    // @Autowired
    private DepartmentRepository departmentRepository;

    // Constructor Injection instead of @Autowired annotation
    public StudentController(StudentRepository studentRepository,
            DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }

    // Show all students
    @RequestMapping(value = "/studentlist")
    public String studentList(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "studentlist";
    }

    // Add new student
    @RequestMapping(value = "/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("departments", departmentRepository.findAll());
        return "addstudent";
    }

    // Save new student
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Student student) {
        studentRepository.save(student);
        return "redirect:studentlist";
    }

    // Delete student
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteStudent(@PathVariable("id") Long studentId, Model model) {
        studentRepository.deleteById(studentId);
        return "redirect:../studentlist";
    }

    // RESTful service to get all students
	@RequestMapping(value="/allStudents", method = RequestMethod.GET)
	public @ResponseBody List<Student> studentListRest() {	
	        return (List<Student>) studentRepository.findAll();
	}    

	// RESTful service to get student by id
	@RequestMapping(value="/allStudents/{id}", method = RequestMethod.GET)
	public @ResponseBody Optional<Student> findStudentRest(@PathVariable("id") Long studentId) {
        return studentRepository.findById(studentId);
	}       
	    
	// RESTful service to save new student
	@RequestMapping(value="/allStudents", method = RequestMethod.POST)
	public @ResponseBody Student saveNewStudentRest(@RequestBody Student student) {
        return studentRepository.save(student);
	}    


}
