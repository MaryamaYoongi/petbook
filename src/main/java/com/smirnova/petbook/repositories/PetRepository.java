package com.smirnova.petbook.repositories;

import com.smirnova.petbook.entities.Pet;
import org.springframework.data.repository.CrudRepository;

public interface PetRepository extends CrudRepository<Pet, Integer> {
}
