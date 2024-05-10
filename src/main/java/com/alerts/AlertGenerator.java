package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */


public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);
        PatientRecord previousRecord = null;
        PatientRecord twoRecordsAgo = null;
        double previousPressure = 0;
        double previousSaturation = 0;

        for(PatientRecord record : records) {
            String recordType = record.getRecordType();
            double measurementValue = record.getMeasurementValue();

            if(recordType.equals("SystolicPressure") || recordType.equals("DiastolicPressure")) {
                if((recordType.equals("SystolicPressure") && (measurementValue > 180 || measurementValue < 90)) || (recordType.equals("DiastolicPressure") && (measurementValue > 120 || measurementValue < 60))){
                    if(recordType.equals("SystolicPressure") && measurementValue < 90){
                        previousPressure = measurementValue;
                    }
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Critical pressure detected", record.getTimestamp()));
                }
                if(previousRecord != null && twoRecordsAgo != null && (previousRecord.getRecordType().equals("SystolicPressure") || previousRecord.getRecordType().equals("DiastolicPressure") && (twoRecordsAgo.getRecordType().equals("SystolicPressure") || twoRecordsAgo.getRecordType().equals("DiastolicPressure")))){
                    if (Math.abs((measurementValue - previousRecord.getMeasurementValue())) >= 10 && Math.abs(previousRecord.getMeasurementValue() - twoRecordsAgo.getMeasurementValue()) >= 10) {
                        triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Trend in blood pressure detected", record.getTimestamp()));
                    }
                }
            } else if(recordType.equals("Saturation")) {
                if(measurementValue < 92) {
                    previousSaturation = measurementValue;
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Low blood saturation detected", record.getTimestamp()));
                }
                if(previousRecord != null && previousRecord.getRecordType().equals("Saturation")){
                    if(record.getTimestamp() - previousRecord.getTimestamp() <= 1000 * 60 * 10 && measurementValue - previousRecord.getMeasurementValue() < -5) {
                        triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Rapid drop in blood saturation detected", record.getTimestamp()));
                    }
                }
            } else if(recordType.equals("ECG")){
                if(measurementValue < 50 || measurementValue > 100) {
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Abnormal heart rate detected", record.getTimestamp()));
                }
                if(previousRecord != null && previousRecord.getRecordType().equals("ECG")){
                    if(Math.abs(measurementValue - previousRecord.getMeasurementValue()) > 10) {
                        triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Irregular heart beat detected", record.getTimestamp()));
                    }
                }
            }
            if(previousPressure != 0 && previousSaturation != 0){
                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Hypotensive hypoxemia detected", record.getTimestamp()));
            }
            twoRecordsAgo = previousRecord;
            previousRecord = record;
        }
    }

    public String evaluateDataString(Patient patient) {
        List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);
        PatientRecord previousRecord = null;
        PatientRecord twoRecordsAgo = null;
        double previousPressure = 0;
        double previousSaturation = 0;

        String result = "";
        for(PatientRecord record : records) {
            String recordType = record.getRecordType();
            double measurementValue = record.getMeasurementValue();

            if(recordType.equals("SystolicPressure") || recordType.equals("DiastolicPressure")) {
                if((recordType.equals("SystolicPressure") && (measurementValue > 180 || measurementValue < 90)) || (recordType.equals("DiastolicPressure") && (measurementValue > 120 || measurementValue < 60))){
                    if(recordType.equals("SystolicPressure") && measurementValue < 90){
                        previousPressure = measurementValue;
                    }
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Critical pressure detected", record.getTimestamp()));
                    result = "Critical pressure detected";
                }
                if(previousRecord != null && twoRecordsAgo != null && (previousRecord.getRecordType().equals("SystolicPressure") || previousRecord.getRecordType().equals("DiastolicPressure") && (twoRecordsAgo.getRecordType().equals("SystolicPressure") || twoRecordsAgo.getRecordType().equals("DiastolicPressure")))){
                    if (Math.abs((measurementValue - previousRecord.getMeasurementValue())) >= 10 && Math.abs(previousRecord.getMeasurementValue() - twoRecordsAgo.getMeasurementValue()) >= 10) {
                        triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Trend in blood pressure detected", record.getTimestamp()));
                        result = "Trend in blood pressure detected";
                    }
                }
            } else if(recordType.equals("Saturation")) {
                if(measurementValue < 92) {
                    previousSaturation = measurementValue;
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Low blood saturation detected", record.getTimestamp()));
                    result = "Low blood saturation detected";
                }
                if(previousRecord != null && previousRecord.getRecordType().equals("Saturation")){
                    if(record.getTimestamp() - previousRecord.getTimestamp() <= 1000 * 60 * 10 && measurementValue - previousRecord.getMeasurementValue() < -5) {
                        triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Rapid drop in blood saturation detected", record.getTimestamp()));
                        result = "Rapid drop in blood saturation detected";
                    }
                }
            } else if(recordType.equals("ECG")){
                if(measurementValue < 50 || measurementValue > 100) {
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Abnormal heart rate detected", record.getTimestamp()));
                    result = "Abnormal heart rate detected";
                }
                if(previousRecord != null && previousRecord.getRecordType().equals("ECG")){
                    if(Math.abs(measurementValue - previousRecord.getMeasurementValue()) > 10) {
                        triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Irregular heart beat detected", record.getTimestamp()));
                        result =  "Irregular heart beat detected";
                    }
                }
            }
            if(previousPressure != 0 && previousSaturation != 0){
                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Hypotensive hypoxemia detected", record.getTimestamp()));
                result = "Hypotensive hypoxemia detected";
            }
            twoRecordsAgo = previousRecord;
            previousRecord = record;
        }
        return result;
    }
    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        Logger logger = Logger.getLogger(AlertGenerator.class.getName());
        logger.log(Level.SEVERE, alert.getPatientId() + ": " + alert.getCondition() + ", at " + alert.getTimestamp());
    }
}
