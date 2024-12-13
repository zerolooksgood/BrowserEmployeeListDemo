package com.zerolooksgood.demo.EmployeeRepositoryDemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;


@Configuration //Defines the class as a configuration so that spring will use it to generate beans
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource datasource) { //Defines this as spring security's users manager, it gets automatically constructed and provided with the datasource which is defined in application.properties
        return new JdbcUserDetailsManager(datasource); //Returns the built-in function for accessing users from the database
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //This is the method that will handle the login and authentication of the users
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/", "/employees/list").hasRole("EMPLOYEE") //Defines what role is required to access this directory
                        .requestMatchers("/employees/showFormForAdd","/employees/showFormForUpdate", "/employees/save").hasRole("MANAGER")
                        .requestMatchers("/employees/showFormForDelete", "/employees/delete").hasRole("ADMINISTRATOR")
                        .anyRequest().authenticated()) //States that any attempt at connecting to the service must be authorized (2)
        .formLogin(form ->
                form
                        .loginPage("/showLoginForm")
                        .loginProcessingUrl("/authenticateTheUser")
                        .permitAll()) //Defines the login and authentication mappings and allows all users to access these even if they're not authorized
        .logout(logout ->
                logout
                        .permitAll()) //Allows everyone to logout
        .exceptionHandling(configurer ->
                configurer
                        .accessDeniedPage("/access-denied")) //Defines the access denied page
        ;

        return http.build(); //Builds the http configurations
    }
}
