package com.smirnova.petbook.controllers;

import com.smirnova.petbook.entities.Pet;
import com.smirnova.petbook.entities.User;
import com.smirnova.petbook.repositories.PetRepository;
import com.smirnova.petbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;


@Controller
@RequestMapping("pet")
public class PetController {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetController(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("add")
    public @ResponseBody
    String addNewPet(@RequestParam String name, @RequestParam String type,
                     @RequestParam(required = false) String breed, @RequestParam(required = false) String hobby,
                     @RequestParam Integer age, @RequestParam String gender, @RequestParam Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Pet k = new Pet();
            setPet(name, age, gender, type, breed, hobby, k);
            User owner = user.get();
            Set<Pet> pets = owner.getPet();

            pets.add(k);
            userRepository.save(owner);
        return "Saved";
        }
        return null;
    }

    @GetMapping("all")
    public @ResponseBody
    Iterable<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @GetMapping("{petId}")
    public @ResponseBody
    Optional<Pet> getPet(@PathVariable int petId) {
        if (petRepository.existsById(petId)) {
            return petRepository.findById(petId);
        }
        return null;
    }

    @GetMapping("/delete/{petId}")
    public @ResponseBody
    String deletePet(@PathVariable int petId) {
        if (petRepository.existsById(petId)) {
            petRepository.deleteById(petId);
            return "Pet " + petId + " has been successfully deleted.";
        } else {
            return "There's no such pet";
        }
    }

    @GetMapping("deleteAll")
    public @ResponseBody
    String deleteAllPets() {
        petRepository.deleteAll();
        return "All pets have been deleted successfully.";
    }

    @GetMapping("update/{petId}")
    public @ResponseBody
    Pet updatePet(@PathVariable Integer petId, @RequestParam(required = false) String name,
                  @RequestParam(required = false) Integer age, @RequestParam(required = false) String gender,
                  @RequestParam(required = false) String type, @RequestParam(required = false) String breed,
                  @RequestParam(required = false) String hobby) {
        Optional<Pet> byId = petRepository.findById(petId);
        if (byId.isPresent()) {
            Pet pet = byId.get();
            setPet(name, age, gender, type, breed, hobby, pet);
            petRepository.save(pet);
            return pet;
        }
        return null;
    }

    private void setPet(String name, Integer age, String gender, String type, String breed, String hobby, Pet pet) {
        pet.setPetName(name);
        pet.setPetAge(age);
        pet.setPetGender(gender);
        pet.setPetType(type);
        pet.setPetBreed(breed);
        pet.setPetHobby(hobby);
    }

}