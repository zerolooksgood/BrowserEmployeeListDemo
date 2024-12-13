package com.zerolooksgood.demo.EmployeeRepositoryDemo.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest //Special test annotation for spring-boot
@AutoConfigureMockMvc //Automatically configures a mock HTTP request
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {} //Checks if the controller loads

    @Nested
    class LogInOutTest {

        @Test
        void shouldRedirectRootToLoginPage() throws Exception {
            mockMvc.perform(get("/")) //Attempts to connect to the {domain}/ using a get method
                    .andExpect(status().is3xxRedirection()) //Checks if the root directory returns a redirection
                    .andExpect(redirectedUrlPattern("**/showLoginForm")); //Checks if redirect returns {domain}/showLoginForm
        }

        @Test
        void shouldReturnLoginPage() throws Exception {
            mockMvc.perform(get("/showLoginForm")) //Attempts to connect to the {domain}/showLoginForm using a get method
                    .andExpect(status().isOk()) //Checks that a connection has been established (HTTP 200)
                    .andExpect(view().name("security/login")); //Checks if the program returns the login  html form
        }

        @Test
        void shouldAuthenticateWithValidCredentials() throws Exception {
            mockMvc.perform(post("/authenticateTheUser").with(csrf()) //Sends a post request with a csrf token to {domain}/authenticateTheUser
                            .param("username", "Administrator") //Appends the username as a parameter
                            .param("password", "Admin123")) //Appends the password as a parameter
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employees/list")); //Checks if the user is redirected to the home page
        }

        @Test
        void shouldNotAuthenticateWithInvalidCredentials() throws Exception {
            mockMvc.perform(post("/authenticateTheUser").with(csrf())
                            .param("username","invalidUsername")
                            .param("password","invalidPassword"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/showLoginForm?error")); //Checks if the user gets denied authentication
        }

        @Test
        void shouldLogout() throws Exception {
            mockMvc.perform(post("/logout").with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/showLoginForm?logout")); //Checks if the user is returned to the login form
        }
    }

    @Nested
    class PermissionTests {
        @Nested
        @WithMockUser(roles = {"EMPLOYEE"})
        class EmployeePermissionTest {

            @Test
            void shouldHaveAccessToRoot() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/employees/list"));
            }

            @Test
            void shouldHaveAccessToHomePage() throws Exception {
                mockMvc.perform(get("/employees/list"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("employees/list-employees"));
            }

            @Test
            void shouldNotHaveAccessToAddPage() throws Exception {
                mockMvc.perform(get("/employees/showFormForAdd"))
                        .andExpect(status().is4xxClientError());
            }

            @Test
            void shouldNotHaveAccessToUpdatePage() throws Exception {
                mockMvc.perform(get("/employees/showFormForUpdate?employeeId=1"))
                        .andExpect(status().is4xxClientError());
            }

            @Test
            void shouldNotHaveAccessToDeletePage() throws Exception {
                mockMvc.perform(get("/employees/showFormForDelete?employeeId=1"))
                        .andExpect(status().is4xxClientError());
            }
        }

        @Nested
        @WithMockUser(roles = {"EMPLOYEE", "MANAGER"})
        class ManagerPermissionTest {

            @Test
            void shouldHaveAccessToRoot() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/employees/list"));
            }

            @Test
            void shouldHaveAccessToHomePage() throws Exception {
                mockMvc.perform(get("/employees/list"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("employees/list-employees"));
            }

            @Test
            void shouldHaveAccessToAddPage() throws Exception {
                mockMvc.perform(get("/employees/showFormForAdd"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("employees/employee-form"));
            }

            @Test
            void shouldHaveAccessToUpdatePage() throws Exception {
                mockMvc.perform(get("/employees/showFormForUpdate?employeeId=1"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("employees/employee-form"));
            }

            @Test
            void shouldNotHaveAccessToDeletePage() throws Exception {
                mockMvc.perform(get("/employees/showFormForDelete?employeeId=1"))
                        .andExpect(status().is4xxClientError());
            }
        }

        @Nested
        @WithMockUser(roles = {"EMPLOYEE", "MANAGER", "ADMINISTRATOR"})
        class AdministratorPermissionTest {

            @Test
            void shouldHaveAccessToRoot() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/employees/list"));
            }

            @Test
            void shouldHaveAccessToHomePage() throws Exception {
                mockMvc.perform(get("/employees/list"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("employees/list-employees"));
            }

            @Test
            void shouldHaveAccessToAddPage() throws Exception {
                mockMvc.perform(get("/employees/showFormForAdd"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("employees/employee-form"));
            }

            @Test
            void shouldHaveAccessToUpdatePage() throws Exception {
                mockMvc.perform(get("/employees/showFormForUpdate?employeeId=1"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("employees/employee-form"));
            }

            @Test
            void shouldHaveAccessToDeletePage() throws Exception {
                mockMvc.perform(get("/employees/showFormForDelete?employeeId=1"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("employees/delete-form"));
            }
        }
    }
}