package com.alerts.decorator;

import com.alerts.Alert;

public class PriorityAlertDecorator extends AlertDecorator{
    private int priority; // 1 = low, 2 = medium, 3 = high

    public PriorityAlertDecorator(Alert alert, int priority){
        super(alert);
        this.priority = priority;
    }

    @Override
    public String getCondition() {
        return alert.getCondition() + " (priority value : " + priority + " !!!)";
    }
}
