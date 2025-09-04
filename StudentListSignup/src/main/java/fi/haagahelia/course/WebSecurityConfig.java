package fi.haagahelia.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig  {
	//@Autowired
	private UserDetailsService userDetailsService; // interface type
	
	// Constructor injection
	public WebSecurityConfig(UserDetailsService userDetailsService) {
			this.userDetailsService = userDetailsService; 
	}
	
	// using lambda 
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/css/**").permitAll()
				.requestMatchers("/signup").permitAll()
				.requestMatchers("/saveuser").permitAll()
				.anyRequest().authenticated()
		)
		.formLogin(formlogin -> formlogin
				.loginPage("/login")
				.defaultSuccessUrl("/studentlist", true)
				.permitAll()
		)
		.logout(logout -> logout
				.permitAll()
		);
				
		return http.build();
	}


	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}}
