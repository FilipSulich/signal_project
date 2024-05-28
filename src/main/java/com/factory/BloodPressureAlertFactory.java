package com.factory;

import com.alerts.generator.BloodPressureAlert;

public class BloodPressureAlertFactory extends AlertFactory{
    @Override
    public BloodPressureAlert createAlert(String patientId, String condition, long timeStamp) {
        return new BloodPressureAlert(patientId, condition, timeStamp);
    }
}
