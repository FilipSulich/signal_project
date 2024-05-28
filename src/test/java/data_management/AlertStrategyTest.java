package data_management;

import com.alerts.strategy.BloodPressureStrategy;
import com.alerts.strategy.OxygenSaturationStrategy;
import com.data_management.Patient;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class AlertStrategyTest {

    @Test
    public void highSystolicPressure(){
        Patient patient = new Patient(1);
        patient.addRecord(190, "SystolicPressure", 1750000000000L);
        BloodPressureStrategy bloodPressureStrategy = new BloodPressureStrategy();
        assertTrue(bloodPressureStrategy.checkAlert(patient));
    }

    @Test
    public void lowSystolicPressure(){
        Patient patient = new Patient(2);
        patient.addRecord(80, "SystolicPressure", 1750000000000L);
        BloodPressureStrategy bloodPressureStrategy = new BloodPressureStrategy();
        assertTrue(bloodPressureStrategy.checkAlert(patient));
    }

    @Test
    public void highDiastolicPressure(){
        Patient patient = new Patient(3);
        patient.addRecord(130, "DiastolicPressure", 1750000000000L);
        BloodPressureStrategy bloodPressureStrategy = new BloodPressureStrategy();
        assertTrue(bloodPressureStrategy.checkAlert(patient));
    }

    @Test
    public void lowDiastolicPressure(){
        Patient patient = new Patient(4);
        patient.addRecord(50, "DiastolicPressure", 1750000000000L);
        BloodPressureStrategy bloodPressureStrategy = new BloodPressureStrategy();
        assertTrue(bloodPressureStrategy.checkAlert(patient));
    }

    @Test
    public void bigDiastolicPressureChange(){
        Patient patient = new Patient(5);
        patient.addRecord(90, "DiastolicPressure", 1750000000000L);
        patient.addRecord(95, "DiastolicPressure", 1750000000000L);
        patient.addRecord(101, "DiastolicPressure", 1750000000000L);
        BloodPressureStrategy bloodPressureStrategy = new BloodPressureStrategy();
        assertTrue(bloodPressureStrategy.checkAlert(patient));
    }

    @Test
    public void bigSystolicPressureChange(){
        Patient patient = new Patient(6);
        patient.addRecord(95, "SystolicPressure", 1750000000000L);
        patient.addRecord(96, "SystolicPressure", 1750000000000L);
        patient.addRecord(111, "SystolicPressure", 1750000000000L);
        BloodPressureStrategy bloodPressureStrategy = new BloodPressureStrategy();
        assertTrue(bloodPressureStrategy.checkAlert(patient));
    }

    @Test
    public void saturationChange(){
        Patient patient = new Patient(7);
        patient.addRecord(90, "Saturation", 1750000000000L);
        patient.addRecord(84, "Saturation", 1750000000000L);
        patient.addRecord(79, "Saturation", 1750000000000L);
        OxygenSaturationStrategy ox = new OxygenSaturationStrategy();
        assertTrue(ox.checkAlert(patient));
    }

    @Test
    public void saturationLow(){
        Patient patient = new Patient(8);
        patient.addRecord(80, "Saturation", 1750000000000L);
        OxygenSaturationStrategy ox = new OxygenSaturationStrategy();
        assertTrue(ox.checkAlert(patient));
    }

}
