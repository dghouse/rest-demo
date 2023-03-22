package ca.dgh.rest.demo.controller.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

/**
 * In the event that a controller returns an empty {@link Optional} object, this adivce will cause the response to be
 * a {@code 404 Not Found}. This allows the application effectively handle empty Optional classes that come from the ORM
 * without explicitly having to throw exceptions on all calls.
 */
@RestControllerAdvice
public class EmptyOptionalResponseControllerAdvice implements ResponseBodyAdvice {

    /**
     * This method tells the system that this advice applies to all controllers that return classes of type {@link Optional}.
     *
     * @param returnType    the return type.
     * @param converterType the selected converter type.
     * @return true if the given returnType passed to this advice is of type {@link Optional}.
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getParameterType().equals(Optional.class);
    }

    /**
     * This method allows us to return an appropriate response if the underlying controller attempts to return an empty
     * {@link Optional} object.
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     * @return the body that was passed in or a modified (possibly new) instance
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (((Optional<?>) body).isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return HttpStatus.NOT_FOUND.toString();
        }
        return body;
    }
}
