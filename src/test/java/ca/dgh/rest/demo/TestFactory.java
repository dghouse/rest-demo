package ca.dgh.rest.demo;

import ca.dgh.rest.demo.model.Province;
import ca.dgh.rest.demo.model.User;
import ca.dgh.rest.demo.model.dto.ProvinceDTO;
import ca.dgh.rest.demo.model.dto.UserDTO;

import java.util.UUID;

public class TestFactory {

    public static Province getProvince() {
        Province province = new Province();
        province.setId(UUID.fromString("480085a9-c3d6-484f-afa2-77ac6f383638"));
        province.setNameEn("Nova Scotia");
        province.setNameFr("Nouvelle-Écosse");
        province.setCode("NS");
        return province;
    }

    public static ProvinceDTO getProvinceDTO() {
        ProvinceDTO province = new ProvinceDTO();
        province.setId(UUID.fromString("08a18487-5ca0-45f6-9fdd-723919132f1c"));
        province.setNameEn("Nova Scotia");
        province.setNameFr("Nouvelle-Écosse");
        province.setCode("NS");
        return province;
    }

    public static User getUser() {
        User user = new User();
        user.setId(UUID.fromString("758517ab-e08a-4910-9446-77d6430ec7ed"));
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setProvinceId(getProvince().getId());
        return user;
    }

    public static UserDTO getUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(UUID.fromString("758517ab-e08a-4910-9446-77d6430ec7ed"));
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setProvinceId(getProvince().getId());
        return user;
    }
}
