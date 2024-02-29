package com.crudtest.democrudtest1.controllers;


import com.crudtest.democrudtest1.entities.User;
import com.crudtest.democrudtest1.repositories.UserRepositories;
import com.crudtest.democrudtest1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Controller
public class UserController {
    @Autowired
    private UserRepositories userRepositories;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userRepositories.findAll();
    }

    @PostMapping("/createUser")
    public @ResponseBody User createUser(@RequestBody User user) {
        return userRepositories.save(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepositories.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepositories.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());

        return userRepositories.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepositories.deleteById(id);
    }
}

