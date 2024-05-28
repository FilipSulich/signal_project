package data_management;

import com.cardiogenerator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.MyWebSocketClient;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.Test;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.net.URI;
import java.util.List;

public class MyWebSocketClientTest {
    public MyWebSocketClient myWebSocketClient;
    public DataStorage mockDataStorage;
    public WebSocketOutputStrategy server;


    @Test
    @DisplayName("Correct addition to dataStorage")
    public void testOnMessage() throws Exception {
        DataStorage mockDataStorage = new DataStorage();
        server = new WebSocketOutputStrategy(8888);
        Thread.sleep(1000);
        myWebSocketClient = new MyWebSocketClient(new URI("ws://localhost:8888"), mockDataStorage);
        myWebSocketClient.connect();
        Thread.sleep(1000);
        server.output(1, 1715269830535L, "Saturation", "90");
        Thread.sleep(1000);
        List<PatientRecord> patientRecords = mockDataStorage.getRecords(1, 1700000000000L, 1800000000000L);
        assertEquals(1, patientRecords.size());
        PatientRecord testPatient = patientRecords.get(0);
        assertEquals(1, testPatient.getPatientId());
        assertEquals(90, testPatient.getMeasurementValue());
        assertEquals("Saturation", testPatient.getRecordType());
        assertEquals(1715269830535L, testPatient.getTimestamp());
    }

//    @Test
//    @DisplayName("Data format test")
//    public void dataFormatTest() throws Exception{
//        DataStorage dataStorage = new DataStorage();
//        server = new WebSocketOutputStrategy(8080);
//        myWebSocketClient = new MyWebSocketClient(new URI("ws://localhost:8080"), mockDataStorage);
//
//        server.output(123, 1727947323000L, "Error", "90%");
//        List<PatientRecord> invalidRecords = dataStorage.getRecords(123, 1727947200000L, 1827947323000L);
//        assertEquals(0, invalidRecords.size());
//    }
}
