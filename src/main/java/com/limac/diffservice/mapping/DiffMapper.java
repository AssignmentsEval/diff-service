package com.limac.diffservice.mapping;

import com.limac.diffservice.domain.Diff;
import com.limac.diffservice.rest.dto.Base64Dto;
import com.limac.diffservice.rest.dto.DiffDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for {@link Diff}.
 */
@Mapper(componentModel = "spring")
public interface DiffMapper {

    @Mapping(source = "base64Value", target = "left")
    Diff base64DtoToLeftDiff(Base64Dto base64Dto);

    @Mapping(source = "base64Value", target = "right")
    Diff base64DtoToRightDiff(Base64Dto base64Dto);

    DiffDto diffToDiffDto(Diff diff);
}
