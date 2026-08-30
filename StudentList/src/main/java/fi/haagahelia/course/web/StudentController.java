package fi.haagahelia.course.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fi.haagahelia.course.domain.StudentRepository;

@Controller
public class StudentController {

    // https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html
    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = { "/", "/studentlist" })
    public String studentList(Model model) {
        model.addAttribute("students", repository.findAll());
        return "studentlist";
    }

}
