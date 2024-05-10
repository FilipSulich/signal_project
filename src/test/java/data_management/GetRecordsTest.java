package data_management;

import com.data_management.Patient;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;

public class GetRecordsTest {

    @Test
    @DisplayName("Get measurement value data test")
    public void getMeasurementValueTest(){
        Patient mockPatient = new Patient(123);
        mockPatient.addRecord(80.0, "SystolicPressure", 1714376789050L);
        Assert.assertEquals(String.valueOf(80.0), String.valueOf(mockPatient.getRecords(1714376789049L, 1714376789051L).get(0).getMeasurementValue()));
    }

    @Test
    @DisplayName("Get record type test")
    public void getRecordTypeTest(){
        Patient mockPatient = new Patient(123);
        mockPatient.addRecord(80.0, "SystolicPressure", 1714376789050L);
        Assert.assertEquals("SystolicPressure", mockPatient.getRecords(1714376789049L, 1714376789051L).get(0).getRecordType());
    }

    @Test
    @DisplayName("Get patient ID test")
    public void getPatientIDTest() {
        Patient mockPatient = new Patient(123);
        mockPatient.addRecord(80.0, "SystolicPressure", 1714376789050L);
        Assert.assertEquals(String.valueOf(123), String.valueOf(mockPatient.getRecords(1714376789049L, 1714376789051L).get(0).getPatientId()));
    }
}
