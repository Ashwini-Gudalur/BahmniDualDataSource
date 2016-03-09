package org.bahmni.custom.data;

import java.sql.Timestamp;

/**
 * Created by sandeepe on 02/03/16.
 */
public class Patient implements BahmniReportObject{

    private int id;
    private String bahmniId;
    private String patientUuid;
    private String gender;
    private String birthDate;
    private String name;
    private String address;
    private String visitStart;
    private String visitEnd;
    private String diagnosis;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    private String date;

    @Override
    public String toString() {
        return "SickleCellPatient{" +
                "id=" + id +
                ", bahmniId='" + bahmniId + '\'' +
                ", patientUuid='" + patientUuid + '\'' +
                ", gender=" + gender +
                '}';
    }

    public String getPatientUuid() {
        return patientUuid;
    }

    public void setPatientUuid(String patientUuid) {
        this.patientUuid = patientUuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBahmniId() {
        return bahmniId;
    }

    public void setBahmniId(String bahmniId) {
        this.bahmniId = bahmniId;
    }


    public Patient() {

    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }


    public void setVisitStart(String visitStart) {
        this.visitStart = visitStart;
    }

    public String getVisitStart() {
        return visitStart;
    }

    public void setVisitEnd(String visitEnd) {
        this.visitEnd = visitEnd;
    }

    public String getVisitEnd() {
        return visitEnd;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis() {
        return diagnosis;
    }
}
