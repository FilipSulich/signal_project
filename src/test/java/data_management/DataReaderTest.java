package data_management;

import com.data_management.DataReader;
import com.data_management.DataReaderClass;
import com.data_management.DataStorage;

import java.util.ArrayList;
import java.util.List;

import com.data_management.PatientRecord;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataReaderTest {


    @Test
    @DisplayName("Data read test")
    public void readDataTest() throws IOException{

        // create test directory with files
        Path mockDirectory = Files.createTempDirectory("mockDirectory");
        Path mockFile1 = Files.createFile(mockDirectory.resolve("mockFile1.txt"));
        Path mockFile2 = Files.createFile(mockDirectory.resolve("mockFile2.txt"));

        List<String> mockLines1 = new ArrayList<>() ;
        mockLines1.add("Patient ID: 1, Timestamp: 1715269830535, Label: DiastolicPressure, Data: 70.0");
        mockLines1.add("Patient ID: 2, Timestamp: 1715269830535, Label: SystolicPressure, Data: 80.0");
        Files.write(mockFile1, mockLines1);

        List<String> mockLines2 = new ArrayList<>() ;
        mockLines1.add("Patient ID: 3, Timestamp: 1715269830535, Label: DiastolicPressure, Data: 90.0");
        mockLines1.add("Patient ID: 4, Timestamp: 1715269830535, Label: SystolicPressure, Data: 100.0");
        Files.write(mockFile2, mockLines2);

        DataStorage dataStorage = new DataStorage();
        DataReader dataReader = new DataReaderClass(mockDirectory.toString());
        dataReader.readData(dataStorage);

        List<PatientRecord> records1 = dataStorage.getRecords(1,1700000000000L, 1800000000000L);
        assertEquals(70.0, records1.get(0).getMeasurementValue());
        assertEquals("DiastolicPressure", records1.get(0).getRecordType());

        List<PatientRecord> records2 = dataStorage.getRecords(2,1700000000000L, 1800000000000L);
        assertEquals(80.0, records2.get(0).getMeasurementValue());
        assertEquals("SystolicPressure", records1.get(0).getRecordType());

        List<PatientRecord> records3 = dataStorage.getRecords(3,1700000000000L, 1800000000000L);
        assertEquals(90.0, records3.get(0).getMeasurementValue());
        assertEquals("DiastolicPressure", records1.get(0).getRecordType());

        List<PatientRecord> records4 = dataStorage.getRecords(4,1700000000000L, 1800000000000L);
        assertEquals(100.0, records4.get(0).getMeasurementValue());
        assertEquals("SystolicPressure", records1.get(0).getRecordType());

    }

}
