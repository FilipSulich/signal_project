package com.alerts.strategy;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class HeartRateStrategy implements AlertStrategy{

    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);
        PatientRecord previousRecord = null;

        /***
         * This won't work because the ECG measurement value is not the heart rate.
         */
        for(PatientRecord record : records) {
            String recordType = record.getRecordType();
            double measurementValue = record.getMeasurementValue();

            if (recordType.equals("ECG")) {
                if (measurementValue < 50 || measurementValue > 100) {
                    return true;
                }
            }
            if(previousRecord != null && previousRecord.getRecordType().equals("ECG")) {
                if (Math.abs(measurementValue - previousRecord.getMeasurementValue()) > 10) {
                    return true;
                }
            }
            previousRecord = record;
        }
        return false;
    }
}
