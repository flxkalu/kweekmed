package com.felixkalu.kweekmed;

public class DiagnosisModel {

    String issueName;
    String accuracy;
    String specialistType;
    String issueId;

    public DiagnosisModel(String issueName, String accuracy, String specialistType, String issueId) {
        this.issueName = issueName;
        this.accuracy = accuracy;
        this.specialistType = specialistType;
        this.issueId = issueId;
    }

    public String getIssueName() {
        return issueName;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
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
