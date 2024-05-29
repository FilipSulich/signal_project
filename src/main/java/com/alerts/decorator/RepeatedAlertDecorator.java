package com.alerts.decorator;

import com.alerts.Alert;

public class RepeatedAlertDecorator extends AlertDecorator{
    private int repeatCount; // number of times the alert is repeated

    public RepeatedAlertDecorator(Alert alert, int repeatCount){
        super(alert);
        this.repeatCount = repeatCount;
    }

    @Override
    public String getCondition() {
        return alert.getCondition() + " (repeated " + repeatCount + " times)";
    }

    public int getRepeatCount() {
        return repeatCount;
    }
}
