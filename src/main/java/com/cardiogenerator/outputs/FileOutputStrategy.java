package com.cardiogenerator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy {

    private String baseDirectory; // changed variable name to lower camel case

    public final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>(); // changed variable name to upper camel case

    /**
     * Constructor for the FileOutputStrategy class, which creates a new instance of the FileOutputStrategy class
     * @param baseDirectory The base directory to write the files to
     */
    public FileOutputStrategy(String baseDirectory) { // class name changed to UpperCamelCase
        this.baseDirectory = baseDirectory;
    }
    /**
     * Output the data to a file
     * @param patientId - the ID of the patient
     * @param timeStamp - the timestamp of the data
     * @param label - the label of the data
     * @param data - the data to output
     * @return void
     */
    @Override
    public void output(int patientId, long timeStamp, String label, String data) { // changed timestamp to timeStamp
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String filePath = FILE_MAP.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString()); // changed variable name to lower camel case
        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timeStamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}