package com.talaria.portal.endpoints;

import com.talaria.portal.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
public class UserEndpoints {

    List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return users;
    }

    @PostMapping("/users")
    public void createNewUser(String name, String medium){
        users.add(new User(name, medium, UUID.randomUUID()));
    }


}