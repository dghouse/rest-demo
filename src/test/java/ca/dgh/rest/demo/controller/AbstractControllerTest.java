package ca.dgh.rest.demo.controller;

import ca.dgh.rest.demo.controller.advice.EmptyOptionalResponseControllerAdvice;
import ca.dgh.rest.demo.model.AbstractEntity;
import ca.dgh.rest.demo.model.dto.AbstractDTO;
import ca.dgh.rest.demo.service.AbstractDTOAwareService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractControllerTest <S extends AbstractEntity, T extends AbstractDTO> {

    /**
     * The testing object for MVC tests.
     */
    @Getter
    private MockMvc mockMvc;

    /**
     * A test object that will be used for testing.
     */
    private T testObject;

    /**
     * The relative root path where the endpoint is being served from.
     */
    private String path;

    /**
     * The mocked service that will provide objects to the controller object.
     */
    private AbstractDTOAwareService<S,T> service;

    /**
     * This method will be executed before each test to set up global variables.
     *
     * @param testObject the test object to use for creating and updating. We expect this class to have a non-null id.
     * @param controller the controller that will allow us to do CRUD operations on the provided object.
     * @param service a mocked service to be used by the controller to preform I/O operations.
     * @param path relative root path where the endpoint is being served from. We expect this string to end with a "/".
     */
    public void setup(T testObject, Controller<T> controller, AbstractDTOAwareService<S,T> service, String path) {
        // Here we check our test data for some basic details that may trip up a developer in the future.
        assert testObject.getId() != null;
        assert path.endsWith("/");

        this.service = service;
        this.path = path;
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new EmptyOptionalResponseControllerAdvice()).build();
        this.testObject = testObject;
        Mockito.when(service.getById(this.testObject.getId())).thenReturn(Optional.of(this.testObject));
    }

    /**
     * Attempt to get a list of all objects from the endpoint.
     * @throws Exception thrown when trying to perform the call to the {@link MockMvc} object.
     */
    @Test
    public void test_getAll() throws Exception {
        Mockito.when(service.getAll()).thenReturn(List.of(testObject));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ObjectMapper mapper = new XmlMapper();
        List<JsonNode> returnedList = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<JsonNode>>() {});

        assert returnedList.size() == 1;
        compare(testObject, returnedList.get(0));
    }

    /**
     * Compare the test object with a {@link JsonNode} representation of that object that has been returned by the
     * {@link MockMvc} object.
     *
     * @param testObject The test object expected to be delivered by the controller.
     * @param jsonNode The JSON node parsed from the {@link MvcResult}.
     */
    public void compare (T testObject, JsonNode jsonNode) {
        assert jsonNode.get("id").asText().equals(testObject.getId().toString());

    }

    /**
     * Test that when a user requests an animal condition given a valid UUID, they actually get that object.
     * @throws Exception thrown when trying to perform the call to the {@link MockMvc} object.
     */
    @Test
    public void test_success_getById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(path + testObject.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        ObjectMapper mapper = new XmlMapper();
        JsonNode returnedObject = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<JsonNode>() {});
        assert returnedObject != null;
        compare(testObject, returnedObject);
    }

    /**
     * If the user requests an animal-condition that does not exit, they should get a "file not found" message. This
     * also exercises the logic in the {@link EmptyOptionalResponseControllerAdvice}.
     * @throws Exception thrown when trying to perform the call to the {@link MockMvc} object.
     */
    @Test
    public void test_failure_getById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(path + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
