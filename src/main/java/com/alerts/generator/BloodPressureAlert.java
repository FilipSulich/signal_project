package com.alerts.generator;

import com.alerts.Alert;

public class BloodPressureAlert extends Alert {
    public BloodPressureAlert(String patientId, String condition, long timestamp){
        super(patientId, condition, timestamp);
    }
}
