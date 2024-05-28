package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyWebSocketClient extends WebSocketClient {
    private DataStorage dataStorage;

    public MyWebSocketClient(URI serverURI) {
        super(serverURI);
        this.dataStorage = DataStorage.getInstance();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("MyClient opened");
        System.out.println("new client opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        try {
            // Parse the message and store the data
            String[] parts = message.split(",");
            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String recordType = parts[2];
            double measurementValue;
            if(parts[3].contains("%")){
                measurementValue = Double.parseDouble(parts[3].substring(0, parts[3].length()-1));
            } else {
                measurementValue = Double.parseDouble(parts[3]);
            }
            // Use the dataStorage instance to store the data
            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
        } catch (Exception e) {
            System.err.println("Error processing message: " + message);
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}
