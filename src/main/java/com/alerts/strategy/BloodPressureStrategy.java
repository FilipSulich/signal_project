package com.alerts.strategy;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy{
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> patientRecords = patient.getRecords(1700000000000L, 1800000000000L);

        //Systolic
        List<PatientRecord> lastThreeSystolicPressureRecords = new ArrayList<>();
        int count = 0;
        int i = 0;
        while (count < 3 && i < patientRecords.size()){
            if (patientRecords.get(i).getRecordType().equals("SystolicPressure")){
                if (patientRecords.get(i).getMeasurementValue() > 180 || patientRecords.get(i).getMeasurementValue() < 90){
                    return true;
                }
                count++;
                lastThreeSystolicPressureRecords.add(patientRecords.get(i));
            }
            i++;
        }
        if (count == 3){
            if (Math.abs(lastThreeSystolicPressureRecords.get(0).getMeasurementValue() - lastThreeSystolicPressureRecords.get(2).getMeasurementValue()) > 10){
                return true;
            }
        }


        //Diastolic
        List<PatientRecord> lastThreeDiastolicPressureRecords = new ArrayList<>();
        count = 0;
        i = 0;
        while (count < 3 && i < patientRecords.size()){
            if (patientRecords.get(i).getRecordType().equals("DiastolicPressure")){
                if (patientRecords.get(i).getMeasurementValue() > 120 || patientRecords.get(i).getMeasurementValue() < 60){
                    return true;
                }
                count++;
                lastThreeDiastolicPressureRecords.add(patientRecords.get(i));
            }
            i++;
        }
        if (count == 3){
            if (Math.abs(lastThreeDiastolicPressureRecords.get(0).getMeasurementValue() - lastThreeDiastolicPressureRecords.get(2).getMeasurementValue()) > 10){
                return true;
            }
        }

        return false;
    }
}
