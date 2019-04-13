package com.smirnova.petbook.controllers;

import com.smirnova.petbook.entities.User;
import com.smirnova.petbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "get-users";
    }

    @GetMapping("{userId}")
    public String getUser(@PathVariable int userId, Model model) throws IllegalAccessException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "get-user";
        } else {
            throw new IllegalArgumentException("User not found!");
        }
    }

    @PostMapping
    public String addNewUser(User user) {
        userRepository.save(user);

        return "get-user";
    }

    @GetMapping("/form")
    public String showAddUserPage(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
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
            user.setUserName(user.getUserName());
            user.setUserAddress(user.getUserAddress());
            user.setUserGender(user.getUserGender());

            user.setUserName(name);
            user.setUserAddress(address);
            user.setUserGender(gender);
            userRepository.save(user);
            return user;
        }
        return null;
    }

}