package com.alerts.strategy;

import com.data_management.Patient;

public interface AlertStrategy {
    boolean checkAlert(Patient patient);
}
