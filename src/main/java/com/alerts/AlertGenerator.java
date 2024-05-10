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

    public static void main(String[] args) {
        DataStorage dataStorage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        Patient patient = new Patient(123);
        PatientRecord record1 = new PatientRecord(123, 100, "SystolicPressure", 1714376789050L);
        PatientRecord record01 = new PatientRecord(123, 110, "SystolicPressure", 1714376789050L);
        PatientRecord record02 = new PatientRecord(123, 120, "SystolicPressure", 1714376789050L);
        PatientRecord record2 = new PatientRecord(123, 90, "DiastolicPressure", 1714376789051L);
        PatientRecord record3 = new PatientRecord(123, 91, "Saturation", 1714376789052L);
        PatientRecord record4 = new PatientRecord(123, 99, "Saturation", 1714376789053L);
        PatientRecord record5 = new PatientRecord(123, 100, "ECG", 1714376789054L);
        PatientRecord record6 = new PatientRecord(123, 50, "ECG", 1714376789055L);
        PatientRecord record7 = new PatientRecord(123, 100, "ECG", 1714376789056L);
        patient.addRecord(record1.getMeasurementValue(), record1.getRecordType(), record1.getTimestamp());
        patient.addRecord(record01.getMeasurementValue(), record01.getRecordType(), record01.getTimestamp());
        patient.addRecord(record02.getMeasurementValue(), record02.getRecordType(), record02.getTimestamp());
        patient.addRecord(record2.getMeasurementValue(), record2.getRecordType(), record2.getTimestamp());
        patient.addRecord(record3.getMeasurementValue(), record3.getRecordType(), record3.getTimestamp());
        patient.addRecord(record4.getMeasurementValue(), record4.getRecordType(), record4.getTimestamp());
        patient.addRecord(record5.getMeasurementValue(), record5.getRecordType(), record5.getTimestamp());
        patient.addRecord(record6.getMeasurementValue(), record6.getRecordType(), record6.getTimestamp());
        patient.addRecord(record7.getMeasurementValue(), record7.getRecordType(), record7.getTimestamp());
        String alert = alertGenerator.evaluateDataString(patient);
        System.out.println(alert);
    }
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
                }
                if(previousRecord != null && twoRecordsAgo != null && (previousRecord.getRecordType().equals("SystolicPressure") || previousRecord.getRecordType().equals("DiastolicPressure") && (twoRecordsAgo.getRecordType().equals("SystolicPressure") || twoRecordsAgo.getRecordType().equals("DiastolicPressure")))){
                    System.out.println("Trend in blood pressure detected");
                    if (Math.abs((measurementValue - previousRecord.getMeasurementValue())) >= 10 && Math.abs(previousRecord.getMeasurementValue() - twoRecordsAgo.getMeasurementValue()) >= 10) {
                        System.out.println("123123123");
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
                    System.out.println("Trend in blood pressure detected");
                    if (Math.abs((measurementValue - previousRecord.getMeasurementValue())) >= 10 && Math.abs(previousRecord.getMeasurementValue() - twoRecordsAgo.getMeasurementValue()) >= 10) {
                        System.out.println("123123123");
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
