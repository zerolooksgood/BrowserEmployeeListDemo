package com.zerolooksgood.demo.EmployeeRepositoryDemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //Defines the class as a configuration so that spring will use it to generate beans
public class SecurityConfig {

    @Bean //Defines the following method/object as a bean
    public InMemoryUserDetailsManager userDetailsManager() { //Defines new user manager in the local application, these users are hardcoded (1)
        UserDetails user1 = User.builder()
                .username("Employee")
                .password("{noop}Admin123")
                .roles("EMPLOYEE")
                .build();
        UserDetails user2 = User.builder()
                .username("Manager")
                .password("{noop}Admin123")
                .roles("EMPLOYEE", "MANAGER")
                .build();
        UserDetails user3 = User.builder()
                .username("Administrator")
                .password("{noop}Admin123")
                .roles("EMPLOYEE", "MANAGER", "ADMINISTRATOR")
                .build();

        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //This is the method that will handle the login and authentication of the users
        http.authorizeHttpRequests(configurer ->
                configurer.anyRequest().authenticated()) //States that any attempt at connecting to the service must be authorized (2)
        .formLogin(form ->
                form.loginPage("/showLoginForm").loginProcessingUrl("/authenticateTheUser").permitAll()) //Defines the login and authentication mappings and allows all users to access these even if they're not authorized
                .logout(logout ->
                        logout.permitAll()); //Allows everyone to logout

        return http.build();
    }
}
