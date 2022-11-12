package ca.dgh.rest.demo.controller;

import ca.dgh.rest.demo.TestFactory;
import ca.dgh.rest.demo.model.Province;
import ca.dgh.rest.demo.model.dto.ProvinceDTO;
import ca.dgh.rest.demo.service.ProvinceService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProvinceControllerTest extends AbstractControllerTest<Province, ProvinceDTO>{

    @Mock
    ProvinceService provinceService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        super.setup(TestFactory.getProvinceDTO(),
                new ProvinceController(provinceService),
                provinceService,
                "/api/province/" );
    }
}
