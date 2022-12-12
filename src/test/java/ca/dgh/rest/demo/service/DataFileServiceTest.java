package ca.dgh.rest.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class DataFileServiceTest {

    @Mock
    UserService userService;
    DataFileService dataFileService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        dataFileService = new DataFileService(userService);
    }

    @Test
    void test() throws FileNotFoundException {
        // TODO: Use the "class path" method to get the actual file.
        FileInputStream fileInputStream = new FileInputStream(new File("users.xlsx"));
        dataFileService.processDataFile(fileInputStream);

    }
}
