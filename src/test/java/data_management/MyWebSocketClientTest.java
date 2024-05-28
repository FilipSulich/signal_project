package data_management;

import com.cardiogenerator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.MyWebSocketClient;
import com.data_management.PatientRecord;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyWebSocketClientTest {
    public MyWebSocketClient myWebSocketClient;
    public DataStorage mockDataStorage;
    public WebSocketOutputStrategy server;


    @Test
    @DisplayName("Correct addition to dataStorage test")
    public void testOnMessage() throws Exception {
        DataStorage mockDataStorage = DataStorage.getInstance();
        server = new WebSocketOutputStrategy(8888);
        Thread.sleep(1000);
        myWebSocketClient = new MyWebSocketClient(new URI("ws://localhost:8888"));
        myWebSocketClient.connect();
        Thread.sleep(1000);
        server.output(1, 1715269830535L, "Saturation", "90%");
        Thread.sleep(1000);
        List<PatientRecord> patientRecords = mockDataStorage.getRecords(1, 1700000000000L, 1800000000000L);
        assertEquals(1, patientRecords.size());
        PatientRecord testPatient = patientRecords.get(0);
        assertEquals(1, testPatient.getPatientId());
        assertEquals(90, testPatient.getMeasurementValue());
        assertEquals("Saturation", testPatient.getRecordType());
        assertEquals(1715269830535L, testPatient.getTimestamp());
    }

    @Test
    @DisplayName("Data format test")
    public void testDataFormat() throws Exception{
        DataStorage dataStorage = DataStorage.getInstance();
        server = new WebSocketOutputStrategy(8080);
        myWebSocketClient = new MyWebSocketClient(new URI("ws://localhost:8080"));

        server.output(123, 1727947323000L, "Error", "90%");
        List<PatientRecord> invalidRecords = dataStorage.getRecords(123, 1727947200000L, 1827947323000L);
        assertEquals(0, invalidRecords.size());
    }
}
