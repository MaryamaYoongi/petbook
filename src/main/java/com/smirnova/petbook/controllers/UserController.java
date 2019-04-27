package com.smirnova.petbook.controllers;

import com.smirnova.petbook.entities.Pet;
import com.smirnova.petbook.entities.User;
import com.smirnova.petbook.repositories.PetRepository;
import com.smirnova.petbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;


@Controller
@RequestMapping("users")
public class UserController {
    private final  PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository, PetRepository petRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "get-users";
    }

    @GetMapping("/{userId}")
    public String getUser(@PathVariable int userId, Model model) throws IllegalAccessException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Set<Pet> pet = userOptional.get().getPets();
            model.addAttribute("user", userOptional.get());
            model.addAttribute("pet",pet);
            return "get-user";
        } else {
            throw new IllegalArgumentException("User not found! Check another ID.");
        }
    }

    @PostMapping
    public String addUser(User user) {
        userRepository.save(user);

        return "get-user";
    }

    @GetMapping("/form")
    public String showAddUserPage(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }



    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable int userId, Model model) throws IllegalAccessException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.deleteById(user.getId());
            return "redirect:/users";
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