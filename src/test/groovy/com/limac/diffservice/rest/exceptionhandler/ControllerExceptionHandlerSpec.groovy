package com.limac.diffservice.rest.exceptionhandler

import com.limac.diffservice.exception.MissingInputException
import com.limac.diffservice.exception.NotFoundException
import com.limac.diffservice.rest.dto.ErrorResponseDto
import org.springframework.core.MethodParameter
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*

class ControllerExceptionHandlerSpec extends Specification {

    ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler()

    def 'exception handler should handle the MethodArgumentNotValidException and send the ErrorResponseDto with the error message'() {
        given: 'a MethodArgumentNotValidException'
        String errorMessage = 'field1 must be valid, field2 must be valid'

        BindingResult bindingResult = Mock(BindingResult)

        FieldError fieldError1 = Mock(FieldError)
        fieldError1.field >> 'field1'
        fieldError1.defaultMessage >> 'must be valid'

        FieldError fieldError2 = Mock(FieldError)
        fieldError2.field >> 'field2'
        fieldError2.defaultMessage >> 'must be valid'

        bindingResult.fieldErrors >> [fieldError1, fieldError2]

        MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException(Mock(MethodParameter), bindingResult)

        when: 'not found is handled'
        ResponseEntity<ErrorResponseDto> responseEntity = controllerExceptionHandler.handleBadRequest(methodArgumentNotValidException)

        then: 'response entity with the errors is returned'
        responseEntity.statusCode == BAD_REQUEST
        responseEntity.body.errorMessage == errorMessage
    }

    def 'exception handler should handle the NotFoundException and send the ErrorResponseDto with the error message'() {
        given: 'a NotFoundException'
        String errorMessage = 'could not find diffId'
        NotFoundException notFoundException = new NotFoundException(errorMessage)

        when: 'not found is handled'
        ResponseEntity<ErrorResponseDto> responseEntity = controllerExceptionHandler.handleNotFound(notFoundException)

        then: 'response entity with the errors is returned'
        responseEntity.statusCode == NOT_FOUND
        responseEntity.body.errorMessage == errorMessage
    }

    def 'exception handler should handle the HttpMediaTypeNotSupportedException and send the ErrorResponseDto with the error message'() {
        given: 'a HttpMediaTypeNotSupportedException'
        String errorMessage = 'Content type \'application/xml\' not supported'
        HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException = new HttpMediaTypeNotSupportedException(errorMessage)

        when: 'unsupported media type is handled'
        ResponseEntity<ErrorResponseDto> responseEntity = controllerExceptionHandler.handleUnsupportedMediaType(httpMediaTypeNotSupportedException)

        then: 'response entity with the errors is returned'
        responseEntity.statusCode == UNSUPPORTED_MEDIA_TYPE
        responseEntity.body.errorMessage == errorMessage
    }

    def 'exception handler should handle the MissingInputException and send the ErrorResponseDto with the error message'() {
        given: 'a MissingInputException'
        String errorMessage = 'errorMessage'
        MissingInputException missingInputException = new MissingInputException(errorMessage)

        when: 'internal server error is handled'
        ResponseEntity<ErrorResponseDto> responseEntity = controllerExceptionHandler.handleInternalServerError(missingInputException)

        then: 'response entity with the errors is returned'
        responseEntity.statusCode == INTERNAL_SERVER_ERROR
        responseEntity.body.errorMessage == errorMessage
    }
}
