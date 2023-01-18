package com.workshop14.addressBook.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class Contact implements Serializable{
    private static final long serialVersionUID = 1L;

    @NotNull(message="Name can not be null")
    @Size(min=3, max=64, message="Name should be between 3 - 64 chars")
    private String name;

    @Email(message="Invalid Email")
    private String email;

    @Size(min=7, message="Phone number must be at least 7 digits")
    private String phoneNumber;

    private String id;

    @Past(message="Date of Birth must not be future")
    @NotNull(message="Date of Birth must be mandatory")
    @DateTimeFormat(pattern="dd-MM-yyyy") // check if this is causing validation to fail
    private LocalDate dateOfBirth;

    private int age;

    public Contact() {
        this.id = this.generateId(8);
    }

    public Contact(String name, String email, String phoneNumber, LocalDate dateOfBirth) {
        this.id = this.generateId(8);
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Contact(String id, String name, String email, String phoneNumber, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }
    
    private synchronized String generateId(int numChars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numChars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numChars);
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        int calculatedAge = 0;
        if ((dateOfBirth != null)) {
            calculatedAge = Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        this.age = calculatedAge;
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}
