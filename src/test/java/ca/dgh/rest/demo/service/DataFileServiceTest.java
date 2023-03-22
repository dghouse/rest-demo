package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.TestFactory;
import ca.dgh.rest.demo.exception.ApiException;
import ca.dgh.rest.demo.exception.ApiExceptionList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DataFileServiceTest {

    @Mock
    UserService userService;
    @Mock
    ProvinceService provinceService;
    DataFileService dataFileService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        dataFileService = new DataFileService(userService, provinceService);
    }

    @Test
    void succeed_processDataFile() throws IOException, ApiExceptionList, ApiException {
        InputStream stream = new ClassPathResource("users.xlsx").getInputStream();
        when(provinceService.getProvinceByName(anyString())).thenReturn(Optional.of(TestFactory.getProvince()));
        dataFileService.processDataFile(stream);
        verify(userService, times(2)).create(any());
    }
}
