package com.cardiogenerator.generators; // package name changed to one not containing an underscore

import java.util.Random; // removed empty line in between two non-static import statements
import com.cardiogenerator.outputs.OutputStrategy;

public class AlertGenerator implements PatientDataGenerator {

    public static final Random RANDOM_GENERATOR = new Random(); // changed variable name to UPPER_SNAKE_CASE
    private boolean[] alertStates; // changed to camelCase, false = resolved, true = pressed

    /**
     * Constructor for the AlertGenerator class, which creates a new instance of the AlertGenerator class
     * @param patientCount - the number of patients
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }
    /**
     * Generate the alert data for a patient and output it. If the alert is already triggered, there is a 90% chance it will resolve.
     * @param patientId - the ID of the patient
     * @param outputStrategy - the output strategy to use
     * @return void
     * @throws Exception if an error occurs while generating the alert data
    */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = 0.1; // changed to lowerCamelCase, Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
