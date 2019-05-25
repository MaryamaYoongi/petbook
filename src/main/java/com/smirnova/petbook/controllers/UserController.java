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
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository, PetRepository petRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("mainPage")
    public String getMainPage(Model model) {
        return "main-page";
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
            User user = userOptional.get();
            model.addAttribute("user", user);
            if (user.getPet().isEmpty()) {
                return "get-user-with-no-pets";
            } else {
                Set pet = petRepository.findAllByOwner(user);
                model.addAttribute("pet", pet);
                return "get-user";
            }
        } else {
            throw new IllegalArgumentException("User not found! Check another ID.");
        }
    }
    
    @GetMapping("{userId}/userPets")
    public String getUserPets(@PathVariable int userId, Model model){
        User user = userRepository.findById(userId).get();
        Set pets = petRepository.findAllByOwner(user);
        model.addAttribute("pets", pets);
        return "get-user-pets";
    }

    @PostMapping
    public String addUser(User user, Model model) {
        user = userRepository.save(user);
        model.addAttribute("user", user);
        return "get-user-with-no-pets";
    }

    @GetMapping("/form")
    public String showAddUserPage(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }


    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable int userId) throws IllegalAccessException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getPet().isEmpty()) {
                Pet pet = (Pet) userOptional.get().getPet();
                petRepository.delete(pet);
            }
            userRepository.delete(user);
            return "redirect:/users";
        } else {
            throw new IllegalAccessException("There's no such user!");
        }
    }

    @GetMapping("deleteAll")
    public String deleteAllUsers(Model model) {
        userRepository.deleteAll();
        petRepository.deleteAll();
        return "deleted";
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