package com.data_management;

import java.io.*;

public class DataReaderFile implements DataReader{
    private String directoryPath;

    public DataReaderFile(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try {
            File file = new File(directoryPath);
            File[] files = file.listFiles();
            for (File f : files) {
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    if (f.isFile() && f.getName().endsWith(".txt") && !f.getName().equals("Alert.txt")){
                        while (reader.ready()) {
                            String line = reader.readLine();
                            String[] data = line.split(" ");
                            int patientID = Integer.parseInt(data[2].substring(0,data[2].length() - 1));
                            double measurementValue = Double.parseDouble(data[8].substring(0,data[8].length() - 1));
                            String recordType = data[6].substring(0, data[6].length() - 1);
                            long timeStamp = Long.parseLong(data[4].substring(0, data[4].length() - 1));
                            dataStorage.addPatientData(patientID, measurementValue, recordType, timeStamp);
                        }
                        reader.close();
                    }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
