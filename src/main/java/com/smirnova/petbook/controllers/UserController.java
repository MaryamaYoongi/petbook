package com.smirnova.petbook.controllers;

import com.smirnova.petbook.entities.User;
import com.smirnova.petbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public @ResponseBody
    String addNewUser(@RequestParam String name
            , @RequestParam String address, @RequestParam String gender) {

        User n = new User();
        n.setUserName(name);
        n.setUserAddress(address);
        n.setUserGender(gender);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping("all")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "profile";
    }

    @GetMapping("{userId}")
    public String getUser(@PathVariable int userId, Model model) throws IllegalAccessException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            ArrayList<Object> users = new ArrayList<>();
            users.add(userOptional.get());
            model.addAttribute("users", users);
            return "profile";
        } else {
            throw new IllegalAccessException("User not found!");
        }
    }

    @GetMapping("/delete/{userId}")
    public @ResponseBody
    String deleteUser(@PathVariable int userId) throws IllegalAccessException {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return "User " + userId + " has been successfully deleted.";
        } else {
            throw new IllegalAccessException("There's no such user!");
        }
    }

    @GetMapping("deleteAll")
    public @ResponseBody
    String deleteAllUsers() {
        userRepository.deleteAll();
        return "All users have been deleted successfully.";
    }

    @GetMapping("update/{userId}")
    public @ResponseBody
    User updateUser(@PathVariable Integer userId, @RequestParam(required = false) String name,
                    @RequestParam(required = false) String address, @RequestParam(required = false) String gender) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            User user = byId.get();

            user.setUserName(name);
            user.setUserAddress(address);
            user.setUserGender(gender);
            userRepository.save(user);
            return user;
        }
        return null;
    }

}