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
        List<PatientRecord> records = patient.getRecords(System.currentTimeMillis() - 1000 * 60 * 60, System.currentTimeMillis());
        PatientRecord previousRecord = null;
        PatientRecord twoRecordsAgo = null;

        for(PatientRecord record : records) {
            if(record.getRecordType().equals("SystolicPressure") || record.getRecordType().equals("DiastolicPressure")) {
              if((record.getRecordType().equals("SystolicPressure") && record.getMeasurementValue() > 180 || record.getMeasurementValue() < 90) || (record.getRecordType().equals("DiastolicPressure") && record.getMeasurementValue() > 120 || record.getMeasurementValue() < 60)){
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Critical pressure detected", record.getTimestamp()));
                }
                if(previousRecord != null && twoRecordsAgo != null &&
                        (record.getMeasurementValue() - previousRecord.getMeasurementValue() > 10 && previousRecord.getMeasurementValue() - twoRecordsAgo.getMeasurementValue() > 10) || (record.getMeasurementValue() - previousRecord.getMeasurementValue() < -10 && (previousRecord.getMeasurementValue() - twoRecordsAgo.getMeasurementValue()) < -10)) {
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Trend in blood pressure detected", record.getTimestamp()));
                }
            } else if(record.getRecordType().equals("Saturation")) {
                if(record.getMeasurementValue() < 92) {
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Low blood saturation detected", record.getTimestamp()));
                }
                if(previousRecord != null && record.getTimestamp() - previousRecord.getTimestamp() <= 1000 * 60 * 10 && record.getMeasurementValue() - previousRecord.getMeasurementValue() < -5) {
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Rapid drop in blood saturation detected", record.getTimestamp()));
                }
            } else if(record.getRecordType().equals("ECG")){
                if(record.getMeasurementValue() < 50 || record.getMeasurementValue() > 100) {
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Abnormal heart rate detected", record.getTimestamp()));
                }
                if(previousRecord != null && Math.abs(record.getMeasurementValue() - previousRecord.getMeasurementValue()) > 10) {
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Irregular heart beat detected", record.getTimestamp()));
                }
            }
            if(previousRecord != null && previousRecord.getRecordType().equals("SystolicPressure") && previousRecord.getMeasurementValue() < 90 && record.getRecordType().equals("Saturation") && record.getMeasurementValue() < 92) {
                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Hypotensive hypoxemia detected", record.getTimestamp()));
            }
            twoRecordsAgo = previousRecord;
            previousRecord = record;
        }
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
