package com.limac.diffservice.rest.exceptionhandler;

import com.limac.diffservice.exception.MissingInputException;
import com.limac.diffservice.exception.NotFoundException;
import com.limac.diffservice.rest.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.StringJoiner;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.*;

/**
 * Controller Exception Handler to map exceptions to {@link ErrorResponseDto}.
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    /**
     * Handles {@link MethodArgumentNotValidException} to send it as an {@link ErrorResponseDto} with status 400.
     *
     * @param exception exception to be handled.
     * @return Response entity containing error response.
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequest(MethodArgumentNotValidException exception) {
        final StringJoiner stringJoiner = new StringJoiner(", ");

        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
            stringJoiner.add(fieldError.getField() + " " + fieldError.getDefaultMessage()));

        final String errorMessage = stringJoiner.toString();

        log.error("{} was caught with {}", kv("exception", exception.getClass().getSimpleName()), kv("errorMessage", errorMessage));

        return ResponseEntity
            .status(BAD_REQUEST)
            .body(ErrorResponseDto.builder().errorMessage(errorMessage).build());
    }


    /**
     * Handles {@link NotFoundException} to send it as an {@link ErrorResponseDto} with status 404.
     *
     * @param exception exception to be handled.
     * @return Response entity containing error response.
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(Exception exception) {
        return buildResponseEntity(exception, NOT_FOUND);
    }

    /**
     * Handles {@link HttpMediaTypeNotSupportedException} to send it as an {@link ErrorResponseDto} with status 415.
     *
     * @param exception exception to be handled.
     * @return Response entity containing error response.
     */
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnsupportedMediaType(Exception exception) {
        return buildResponseEntity(exception, UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Handles {@link MissingInputException} to send it as an {@link ErrorResponseDto} with status 500.
     * This handler is also used as fallback for any unexpected {@link Exception}.
     *
     * @param exception exception to be handled.
     * @return Response entity containing error response.
     */
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ Exception.class, MissingInputException.class })
    public ResponseEntity<ErrorResponseDto> handleInternalServerError(Exception exception) {
        return buildResponseEntity(exception, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponseDto> buildResponseEntity(Exception exception, HttpStatus httpStatus) {
        final String errorMessage = exception.getMessage();

        log.error("{} was caught with {}", kv("exception", exception.getClass().getSimpleName()), kv("errorMessage", errorMessage));

        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResponseDto.builder().errorMessage(errorMessage).build());
    }
}
