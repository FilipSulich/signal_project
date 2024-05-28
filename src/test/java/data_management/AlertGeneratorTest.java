package data_management;

import com.alerts.generator.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;

public class AlertGeneratorTest {
    public DataStorage mockDataStorage;
    public AlertGenerator mockAlertGenerator = new AlertGenerator(mockDataStorage);
    public Patient mockPatient = new Patient(123);

    @Test
    @DisplayName("Critical pressure test")
    public void evaluatePressure(){
        PatientRecord mockRecord = new PatientRecord(123, 80, "SystolicPressure", 1714376789050L);
        mockPatient.addRecord(mockRecord.getMeasurementValue(), mockRecord.getRecordType(), mockRecord.getTimestamp());
        String alert = mockAlertGenerator.evaluateDataString(mockPatient);
        Assert.assertEquals("Critical pressure detected", alert);
    }
    @Test
    @DisplayName("Hypotensive hypoxemia test")
    public void evaluateHypotensiveHypoxemia(){
        PatientRecord mockRecord = new PatientRecord(123, 80, "SystolicPressure", 1714376789050L);
        PatientRecord mockRecord2 = new PatientRecord(123, 90, "DiastolicPressure", 1714376789051L);
        PatientRecord mockRecord3 = new PatientRecord(123, 91, "Saturation", 1714376789052L);
        mockPatient.addRecord(mockRecord.getMeasurementValue(), mockRecord.getRecordType(), mockRecord.getTimestamp());
        mockPatient.addRecord(mockRecord2.getMeasurementValue(), mockRecord2.getRecordType(), mockRecord2.getTimestamp());
        mockPatient.addRecord(mockRecord3.getMeasurementValue(), mockRecord3.getRecordType(), mockRecord3.getTimestamp());
        String alert = mockAlertGenerator.evaluateDataString(mockPatient);
        Assert.assertEquals("Hypotensive hypoxemia detected", alert);
    }
    @Test
    @DisplayName("Trend in blood pressure test")
    public void evaluateTrend(){
        PatientRecord mockRecord = new PatientRecord(123, 100, "SystolicPressure", 1714376789050L);
        PatientRecord mockRecord1 = new PatientRecord(123, 110, "SystolicPressure", 1714376789050L);
        PatientRecord mockRecord2 = new PatientRecord(123, 120, "SystolicPressure", 1714376789051L);
        mockPatient.addRecord(mockRecord.getMeasurementValue(), mockRecord.getRecordType(), mockRecord.getTimestamp());
        mockPatient.addRecord(mockRecord1.getMeasurementValue(), mockRecord1.getRecordType(), mockRecord1.getTimestamp());
        mockPatient.addRecord(mockRecord2.getMeasurementValue(), mockRecord2.getRecordType(), mockRecord2.getTimestamp());
        String alert = mockAlertGenerator.evaluateDataString(mockPatient);
        Assert.assertEquals("Trend in blood pressure detected", alert);
    }
    @Test
    @DisplayName("Low blood saturation test")
    public void evaluateLowSaturation(){
        PatientRecord mockRecord3 = new PatientRecord(123, 91, "Saturation", 1714376789052L);
        mockPatient.addRecord(mockRecord3.getMeasurementValue(), mockRecord3.getRecordType(), mockRecord3.getTimestamp());
        String alert = mockAlertGenerator.evaluateDataString(mockPatient);
        Assert.assertEquals("Low blood saturation detected", alert);
    }

    @Test
    @DisplayName("Rapid drop in blood saturation test")
    public void evaluateRapidDrop(){
        PatientRecord mockRecord3 = new PatientRecord(123, 99, "Saturation", 1714376789052L);
        PatientRecord mockRecord4 = new PatientRecord(123, 93, "Saturation", 1714376789053L);
        mockPatient.addRecord(mockRecord3.getMeasurementValue(), mockRecord3.getRecordType(), mockRecord3.getTimestamp());
        mockPatient.addRecord(mockRecord4.getMeasurementValue(), mockRecord4.getRecordType(), mockRecord4.getTimestamp());
        String alert = mockAlertGenerator.evaluateDataString(mockPatient);
        Assert.assertEquals("Rapid drop in blood saturation detected", alert);
    }
    @Test
    @DisplayName("Abnormal heart rate test")
    public void evaluateHeartRate(){
        PatientRecord mockRecord = new PatientRecord(123, 40, "ECG", 1714376789054L);
        mockPatient.addRecord(mockRecord.getMeasurementValue(), mockRecord.getRecordType(), mockRecord.getTimestamp());
        String alert = mockAlertGenerator.evaluateDataString(mockPatient);
        Assert.assertEquals("Abnormal heart rate detected", alert);
    }
    @Test
    @DisplayName("Irregular heart beat test")
    public void evaluateIrregularHeartBeat(){
        PatientRecord mockRecord = new PatientRecord(123, 60, "ECG", 1714376789054L);
        PatientRecord mockRecord2 = new PatientRecord(123, 70, "ECG", 1714376789055L);
        mockPatient.addRecord(mockRecord.getMeasurementValue(), mockRecord.getRecordType(), mockRecord.getTimestamp());
        mockPatient.addRecord(mockRecord2.getMeasurementValue(), mockRecord2.getRecordType(), mockRecord2.getTimestamp());
        String alert = mockAlertGenerator.evaluateDataString(mockPatient);
        Assert.assertEquals("Irregular heart beat detected", alert);
    }
}
