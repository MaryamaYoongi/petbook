package com.smirnova.petbook.entities;

import javax.persistence.*;
import java.text.MessageFormat;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer petId;
    private String petName;
    private Integer petAge;
    private String petGender;
    private String petBreed;
    private String petType;
    private String petHobby;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public Integer getId() {
        return petId;
    }

    public void setId(Integer id) {
        this.petId = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Integer getPetAge() {
        return petAge;
    }

    public void setPetAge(Integer petAge) {
        this.petAge = petAge;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetHobby() {
        return petHobby;
    }

    public void setPetHobby(String petHobby) {
        this.petHobby = petHobby;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {

        return MessageFormat.format("\n  Name: {0};\n  Type: {1};\n  Age: {2};\n",
                this.petName, this.petType, this.petAge);
    }
}