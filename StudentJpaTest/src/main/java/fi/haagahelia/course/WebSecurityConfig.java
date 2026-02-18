package fi.haagahelia.course;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
	

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(
				authorize -> authorize.requestMatchers("/css/**").permitAll().anyRequest().authenticated())
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions // for h2console
						.disable()))
				.formLogin(
						formlogin -> formlogin.defaultSuccessUrl("/studentlist", true).permitAll())
				.logout(logout -> logout.permitAll());

		return http.build();
	}

	@Bean BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
