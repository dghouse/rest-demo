package ca.dgh.rest.demo.model.dto;

import java.util.UUID;

public interface LookupDTO {
    UUID getId();
    String getCode();
    String getNameEn();
    String getNameFr();
    void setId(UUID id);
    void setCode(String code);
    void setNameEn(String nameEn);
    void setNameFr(String nameFr);
}
