package com.felixkalu.kweekmed;

import java.net.URI;
import java.net.URL;
import java.util.Date;

public class DoctorsModel {

    private String name, surname, sex, age, specialty, currentHospitalOfService,
            yearsOfExperience, email, primaryMobileNumber, medicalCertificateLink, photoLink, description, location, id  ;

    public DoctorsModel(String name, String surname, String sex, String age, String specialty, String currentHospitalOfService, String yearsOfExperience, String email, String primaryMobileNumber, String medicalCertificateLink, String photoLink, String description, String location, String id) {
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.age = age;
        this.specialty = specialty;
        this.currentHospitalOfService = currentHospitalOfService;
        this.yearsOfExperience = yearsOfExperience;
        this.email = email;
        this.primaryMobileNumber = primaryMobileNumber;
        this.medicalCertificateLink = medicalCertificateLink;
        this.photoLink = photoLink;
        this.description = description;
        this.location = location;
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedicalCertificateLink() {
        return medicalCertificateLink;
    }

    public void setMedicalCertificateLink(String medicalCertificateLink) {
        this.medicalCertificateLink = medicalCertificateLink;
    }

}
