package com.factory;

import com.alerts.generator.BloodOxygenAlert;

public class BloodOxygenAlertFactory extends AlertFactory{
    @Override
    public BloodOxygenAlert createAlert(String patientId, String condition, long timeStamp) {
        return new BloodOxygenAlert(patientId, condition, timeStamp);
    }
}
