package com.alerts.strategy;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy{
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);
        PatientRecord previousRecord = null;

        for(PatientRecord record : records) {
            String recordType = record.getRecordType();
            double measurementValue = record.getMeasurementValue();

            if (recordType.equals("Saturation")) {
                if (measurementValue < 92) {
                    return true;
                }
            }
            if (previousRecord != null && previousRecord.getRecordType().equals("Saturation")) {
                if (record.getTimestamp() - previousRecord.getTimestamp() <= 1000 * 60 * 10 && measurementValue - previousRecord.getMeasurementValue() < -5) {
                    return true;
                }
            }
            previousRecord = record;
        }
        return false;
    }
}
