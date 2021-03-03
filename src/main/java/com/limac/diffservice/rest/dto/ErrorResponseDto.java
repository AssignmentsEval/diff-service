package com.limac.diffservice.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Data Transfer Object to hold the errors.
 */
@Data
@Builder
@JsonInclude(NON_NULL)
public class ErrorResponseDto {

    private String errorMessage;
}
