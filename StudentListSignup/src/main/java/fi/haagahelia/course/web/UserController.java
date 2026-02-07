package fi.haagahelia.course.web;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.haagahelia.course.domain.AppUser;
import fi.haagahelia.course.domain.AppUserRepository;
import fi.haagahelia.course.domain.SignupForm;
import jakarta.validation.Valid;

@Controller
public class UserController {

    private final AppUserRepository repository; 
	
	//Constructor Injection
	public UserController(AppUserRepository appUserReporitory) {
		this.repository = appUserReporitory; 
	}
	
    @RequestMapping(value = "signup")
    public String addStudent(Model model){
    	model.addAttribute("signupform", new SignupForm());
        return "signup";
    }	
    
    /**
     * Create new user
     * Check if user already exists & form validation
     * 
     * @param signupForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "saveuser", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult) {
    	if (!bindingResult.hasErrors()) { // validation errors
    		if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) { // check password match		
	    		String pwd = signupForm.getPassword();
		    	BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		    	String hashPwd = bc.encode(pwd);
	
		    	AppUser newUser = new AppUser();
		    	newUser.setPasswordHash(hashPwd);
		    	newUser.setUsername(signupForm.getUsername());
		    	newUser.setRole("USER");
		    	if (repository.findByUsername(signupForm.getUsername()) == null) { // Check if user exists
		    		repository.save(newUser);
		    	}
		    	else {
	    			bindingResult.rejectValue("username", "err.username", "Username already exists");    	
	    			return "signup";		    		
		    	}
    		}
    		else {
    			bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords does not match");    	
    			return "signup";
    		}
    	}
    	else {
    		return "signup";
    	}
    	return "redirect:/login";    	
    }    
    
}
