package com.felixkalu.kweekmed;


public class SymptomModel {

    boolean isSelected;
    String symptomName;
    String symptomId;

    public SymptomModel(boolean isSelected, String symptomName, String symptomId) {
        this.isSelected = isSelected;
        this.symptomName = symptomName;
        this.symptomId = symptomId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return symptomName;
    }

    public void setSymptomName(String symptomName) {
        this.symptomName = symptomName;
    }

    public String getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(String symptomId) {
        this.symptomId = symptomId;
    }
}
