package com.felixkalu.kweekmed;

import java.net.URI;
import java.net.URL;
import java.util.Date;

public class DoctorsModel {

    private String name, surname, age,sex, specialty, currentHospitalOfService,
            yearsOfExperience, email, primaryMobileNumber, photoLink, id, location;

    public DoctorsModel(String photoLink,String name, String specialty, String location) {

        this.photoLink = photoLink;
        this.name = name;
        this.specialty = specialty;
        this.location = location;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getCurrentHospitalOfService() {
        return currentHospitalOfService;
    }

    public void setCurrentHospitalOfService(String currentHospitalOfService) {
        this.currentHospitalOfService = currentHospitalOfService;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrimaryMobileNumber() {
        return primaryMobileNumber;
    }

    public void setPrimaryMobileNumber(String primaryMobileNumber) {
        this.primaryMobileNumber = primaryMobileNumber;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
