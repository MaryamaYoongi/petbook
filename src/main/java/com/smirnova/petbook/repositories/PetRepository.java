package com.smirnova.petbook.repositories;

import com.smirnova.petbook.entities.Pet;
import com.smirnova.petbook.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PetRepository extends CrudRepository<Pet, Integer> {
    Set findAllByOwner(User owner);
}
