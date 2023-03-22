package ca.dgh.rest.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@AllArgsConstructor
@Getter
@Setter
public class ApiException extends Exception {
    String message;

    Optional<Object> value;

    public ApiException(String message) {
        this.message = message;
        this.value = Optional.empty();
    }
}
