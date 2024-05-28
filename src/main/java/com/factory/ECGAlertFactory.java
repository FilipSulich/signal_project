package com.factory;

import com.alerts.generator.ECGAlert;

public class ECGAlertFactory extends AlertFactory{
    @Override
    public ECGAlert createAlert(String patientId, String condition, long timeStamp) {
        return new ECGAlert(patientId, condition, timeStamp);
    }
}
