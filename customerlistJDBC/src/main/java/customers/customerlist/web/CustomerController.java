package customers.customerlist.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import customers.customerlist.domain.Customer;
import customers.customerlist.domain.CustomerRepository;



@Controller
public class CustomerController {

    private CustomerRepository customerRepository;

    // Constructor injection
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        List<Customer> bookList = (ArrayList<Customer>) customerRepository.findAll();
        System.out.println(bookList.toString());
        return "index";
    }

    @RequestMapping(value = "/customerlist", method = RequestMethod.GET)
    public String showCustomers(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        
        return "customerlist";
    }

    @RequestMapping(value = "/customerform", method = RequestMethod.POST)
    public String addCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        return "newcustomer";
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveCustomer(Customer customer) {
        customerRepository.save(customer);
        return "redirect:customerlist";
    }

}
