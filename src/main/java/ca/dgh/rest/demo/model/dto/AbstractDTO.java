package ca.dgh.rest.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class AbstractDTO {
    private UUID id;
}
