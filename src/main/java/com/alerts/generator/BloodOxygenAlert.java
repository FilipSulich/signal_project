package com.alerts.generator;

import com.alerts.Alert;

public class BloodOxygenAlert extends Alert {
    public BloodOxygenAlert(String patientId, String condition, long timestamp){
        super(patientId, condition, timestamp);
    }
}
