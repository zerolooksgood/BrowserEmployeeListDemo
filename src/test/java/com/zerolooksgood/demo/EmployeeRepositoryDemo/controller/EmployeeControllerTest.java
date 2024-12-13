package com.zerolooksgood.demo.EmployeeRepositoryDemo.controller;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest //Special test annotation for spring-boot
@AutoConfigureMockMvc //Automatically configures a mock HTTP request
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void contextLoads() {} //Checks if the controller loads

    @Nested //A "package" of tests
    @WithMockUser(roles = {"EMPLOYEE", "MANAGER", "ADMINISTRATOR"}) //All these tests are execute with authentication so that they don't have to log in again
    class CheckAllPages {

        @Test
        void shouldRedirectRootToHome() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employees/list"));
        }

        @Test
        void shouldReceiveHomePage() throws Exception {
            mockMvc.perform(get("/employees/list"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("employees/list-employees"));
        }

        @Test
        void shouldReceiveAddPage() throws Exception {
            mockMvc.perform(get("/employees/showFormForAdd"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("employees/employee-form"));
        }

        @Test
        void shouldReceiveUpdatePage() throws Exception {
            mockMvc.perform(get("/employees/showFormForUpdate?employeeId=1")) //Uses the hidden example user as the target when testing the pages
                    .andExpect(status().isOk())
                    .andExpect(view().name("employees/employee-form"));

        }

        @Test
        void shouldReceiveDeletePage() throws Exception {
            mockMvc.perform(get("/employees/showFormForDelete?employeeId=1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("employees/delete-form"));
        }

        @Test
        void shouldReceiveAccessDeniedPage() throws Exception {
            mockMvc.perform(get("/access-denied"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("error-pages/error-page"));
        }
    }
}