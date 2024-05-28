package com.alerts.generator;

import com.alerts.Alert;

public class ECGAlert extends Alert {
    public ECGAlert(String patientId, String condition, long timestamp){
        super(patientId, condition, timestamp);
    }
}
