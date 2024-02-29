package com.crudtest.democrudtest1;

import com.crudtest.democrudtest1.controllers.UserController;
import com.crudtest.democrudtest1.entities.User;
import com.crudtest.democrudtest1.repositories.UserRepositories;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;


import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value ="test")
@AutoConfigureMockMvc
public class ControllerTest {
    @LocalServerPort/* springboot va a iniettare all'interno della variabile sotto il numero della port */
    private int port;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserController controller;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserRepositories userRepositories;

    @Test
    void userControllerLoads() {
        assertThat(controller).isNotNull();
    }
    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepositories.findAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("user2"));

        verify(userRepositories, times(1)).findAll();
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");

        when(userRepositories.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user1@example.com"));

        verify(userRepositories, times(1)).findById(1L);
    }
    @Test
    public void createUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");

        when(userRepositories.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"user1\", \"email\": \"user1@example.com\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user1@example.com"));

        verify(userRepositories, times(1)).save(any(User.class));
    }
    @Test
    public void testUpdateUser() throws Exception {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("user1");
        existingUser.setEmail("user1@example.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("user1_updated");
        updatedUser.setEmail("user1_updated@example.com");

        when(userRepositories.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepositories.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"user1_updated\", \"email\": \"user1_updated@example.com\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user1_updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user1_updated@example.com"));

        verify(userRepositories, times(1)).findById(1L);
        verify(userRepositories, times(1)).save(any(User.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userRepositories, times(1)).deleteById(1L);
    }

}