package com.talaria.portal.endpoints;

import com.talaria.portal.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/users/{uuid}")
    public ResponseEntity<User> getUser(@PathVariable UUID uuid){
        var user = users.stream().filter(u -> u.uuid().equals(uuid)).findFirst();
        if (user.isPresent()){
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users/{uuid}")
    public ResponseEntity<User> deleteUser(@PathVariable UUID uuid){
        var user = users.stream().filter(u -> u.uuid().equals(uuid)).findFirst();
        if (user.isPresent()){
            users.remove(user.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/users")
    public void createNewUser(String name, String medium){
        users.add(new User(name, medium, UUID.randomUUID()));
    }


}