package com.crudtest.democrudtest1.services;


import com.crudtest.democrudtest1.entities.User;
import com.crudtest.democrudtest1.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepositories userRepositories;


}
