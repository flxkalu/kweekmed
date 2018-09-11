package com.felixkalu.kweekmed;

public class DiagnosisModel {

    String issueName;
    String accuracy;
    String specialistType;

    public DiagnosisModel(String issueName, String accuracy, String specialistType) {
        this.issueName = issueName;
        this.accuracy = accuracy;
        this.specialistType = specialistType;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getSpecialistType() {
        return specialistType;
    }

    public void setSpecialistType(String specialistType) {
        this.specialistType = specialistType;
    }
}
