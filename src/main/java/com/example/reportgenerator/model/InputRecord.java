package com.example.reportgenerator.model;

import lombok.Data;

@Data
public class InputRecord {

    private String field1;

    private String field2;

    private String field3;

    private Double field5;

    private String refkey1;

    private String refkey2;

    public InputRecord(String field1, String field2, String field3,
                       Double field5, String refkey1, String refkey2) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field5 = field5;
        this.refkey1 = refkey1;
        this.refkey2 = refkey2;
    }

    public InputRecord()
    {

    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public Double getField5() {
        return field5;
    }

    public void setField5(Double field5) {
        this.field5 = field5;
    }

    public String getRefkey1() {
        return refkey1;
    }

    public void setRefkey1(String refkey1) {
        this.refkey1 = refkey1;
    }

    public String getRefkey2() {
        return refkey2;
    }

    public void setRefkey2(String refkey2) {
        this.refkey2 = refkey2;
    }

    @Override
    public String toString() {
        return "InputRecord{" +
                "field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                ", field3='" + field3 + '\'' +
                ", field5=" + field5 +
                ", refkey1='" + refkey1 + '\'' +
                ", refkey2='" + refkey2 + '\'' +
                '}';
    }
}