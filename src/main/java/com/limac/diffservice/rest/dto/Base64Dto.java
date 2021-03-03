package com.limac.diffservice.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.limac.diffservice.validation.annotation.Base64;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Data transfer object to hold the base64 value to be stored.
 */
@Data
@JsonInclude(NON_NULL)
public class Base64Dto {

    @Base64
    private String base64Value;
}
