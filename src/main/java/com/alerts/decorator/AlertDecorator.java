package com.alerts.decorator;

import com.alerts.Alert;

public class AlertDecorator extends Alert {
    public Alert alert;
    public AlertDecorator(Alert alert) {
        super(alert.getPatientId(), alert.getCondition(), alert.getTimestamp());
    }
}
