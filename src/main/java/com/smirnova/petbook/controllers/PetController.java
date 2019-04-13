package com.smirnova.petbook.controllers;

import com.smirnova.petbook.entities.Pet;
import com.smirnova.petbook.repositories.PetRepository;
import com.smirnova.petbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("pets")
public class PetController {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetController(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public String addNewPet(Pet pet) {
            petRepository.save(pet);
            return "get-pet";
        }

    @GetMapping("/form")
    public String showAddPetPage(Model model) {
        model.addAttribute("pet", new Pet());
        return "addPet";
    }

    @GetMapping
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
            pet.setPetName(pet.getPetName());
            pet.setPetAge(pet.getPetAge());
            pet.setPetGender(pet.getPetGender());
            pet.setPetType(pet.getPetType());
            pet.setPetBreed(pet.getPetBreed());
            pet.setPetHobby(pet.getPetHobby());
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