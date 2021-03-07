package com.limac.diffservice.mapping;

import com.limac.diffservice.domain.Diff;
import com.limac.diffservice.rest.dto.Base64Dto;
import com.limac.diffservice.rest.dto.DiffDto;
import com.limac.diffservice.rest.dto.DiffDto.DiffDtoBuilder;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-06T10:53:13+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_282 (Oracle Corporation)"
)
@Component
public class DiffMapperImpl implements DiffMapper {

    @Override
    public Diff base64DtoToLeftDiff(Base64Dto base64Dto) {
        if ( base64Dto == null ) {
            return null;
        }

        Diff diff = new Diff();

        diff.setLeft( base64Dto.getBase64Value() );

        return diff;
    }

    @Override
    public Diff base64DtoToRightDiff(Base64Dto base64Dto) {
        if ( base64Dto == null ) {
            return null;
        }

        Diff diff = new Diff();

        diff.setRight( base64Dto.getBase64Value() );

        return diff;
    }

    @Override
    public DiffDto diffToDiffDto(Diff diff) {
        if ( diff == null ) {
            return null;
        }

        DiffDtoBuilder diffDto = DiffDto.builder();

        diffDto.diffId( diff.getDiffId() );
        diffDto.left( diff.getLeft() );
        diffDto.right( diff.getRight() );

        return diffDto.build();
    }
}
