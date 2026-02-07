package fi.haagahelia.course;

//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
	
	//@Autowired
	//private UserDetailsService userDetailsService; // type of attribute -> interface
	
	// Constructor injection
	//public WebSecurityConfig(UserDetailsService userDetailsService) {
//		this.userDetailsService = userDetailsService; 
//	}
 	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(
				authorize -> authorize
				.requestMatchers("/css/**").permitAll()
				.requestMatchers("/api/students**").permitAll()
				.requestMatchers("/h2-console/**").permitAll() // for h2console
				.anyRequest().authenticated())
				// Käyttää HTTP Basic -autentikointia oletusasetuksilla (Postman)
                .httpBasic(Customizer.withDefaults())
				.headers(headers -> 
					headers.frameOptions(frameOptions -> frameOptions 
						.disable())) // for h2console
				.formLogin(formlogin -> 
					formlogin.loginPage("/login")
					.defaultSuccessUrl("/studentlist", true)
					.permitAll())
				.logout(logout -> logout.permitAll())
				.csrf(csrf -> csrf.disable()); // not for production, just for development
		return http.build();
	}

}
