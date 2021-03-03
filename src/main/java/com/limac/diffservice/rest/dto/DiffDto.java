package com.limac.diffservice.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.limac.diffservice.type.ResultType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;


/**
 * Data transfer object to hold the inputs to be compared and the results of the comparison.
 */
@Data
@Builder
@JsonInclude(NON_NULL)
public class DiffDto {

    private String diffId;

    private String left;

    private String right;

    private ResultType resultType;

    private List<DiffResultDto> diffResults;
}
