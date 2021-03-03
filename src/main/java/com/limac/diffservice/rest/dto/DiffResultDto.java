package com.limac.diffservice.rest.dto;

import com.limac.diffservice.domain.Diff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Data transfer object to hold the results from the {@link Diff} comparison.
 */
@Data
@Builder
@AllArgsConstructor
public class DiffResultDto {

    private Integer offset;

    private Integer length;
}
