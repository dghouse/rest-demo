package ca.dgh.rest.demo.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ApiExceptionList extends Throwable {
    @Getter
    @Setter
    List<ApiException> exceptions;

    public ApiExceptionList() {
        exceptions = new ArrayList<>();
    }

    public void add(ApiException exception) {
        this.exceptions.add(exception);
    }

    public void addAll(List<ApiException> exceptions) {
        this.exceptions.addAll(exceptions);
    }
}
