package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.TestFactory;
import ca.dgh.rest.demo.mapper.ModelMapperConfiguration;
import ca.dgh.rest.demo.model.Province;
import ca.dgh.rest.demo.model.dto.ProvinceDTO;
import ca.dgh.rest.demo.repository.ProvinceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class ProvinceServiceTest extends AbstractDTOAwareServiceTest<Province, ProvinceDTO>{

    @Mock
    private ProvinceRepository productRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ModelMapperConfiguration modelMapperConfiguration = new ModelMapperConfiguration();
        ModelMapper modelMapper = modelMapperConfiguration.createModelMapper();
        super.setup(new ProvinceService(productRepository, modelMapper), productRepository,
                modelMapper,
                TestFactory.getProvince(),
                Province.class, ProvinceDTO.class);
    }
}
