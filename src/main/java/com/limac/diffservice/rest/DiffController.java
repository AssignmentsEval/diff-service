package com.limac.diffservice.rest;

import com.limac.diffservice.domain.Diff;
import com.limac.diffservice.mapping.DiffMapper;
import com.limac.diffservice.rest.dto.DiffDto;
import com.limac.diffservice.rest.dto.ErrorResponseDto;
import com.limac.diffservice.rest.dto.Base64Dto;
import com.limac.diffservice.service.DiffService;
import com.limac.diffservice.type.InputType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for Diff.
 */
@Api(tags = "Diff Controller")
@Slf4j
@RestController
@RequestMapping(path = "/v1")
@RequiredArgsConstructor
public class DiffController {

    private static final String DIFF_ID = "diffId";

    private final DiffService diffService;
    private final DiffMapper diffMapper;

    /**
     * Encodes the request body and set it as left attribute from {@link Diff}.
     *
     * @param diffId diff identifier.
     * @return {@link DiffDto}.
     */
    @PostMapping(path = "diff/{diffId}/left", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Saves the request body encoded in base64 as left attribute.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = DiffDto.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponseDto.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 415, message = "Unsupported Media Type", response = ErrorResponseDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    public DiffDto saveLeft(
        @ApiParam(name = DIFF_ID, required = true)
        @PathVariable String diffId,
        @ApiParam(name = "left", required = true)
        @Valid @RequestBody Base64Dto base64Dto) {

        log.info("Received {} for {}", kv("left", base64Dto.getBase64Value()), kv(DIFF_ID, diffId));

        final Diff diff = diffMapper.base64DtoToLeftDiff(base64Dto);
        diff.setDiffId(diffId);

        return diffMapper.diffToDiffDto(diffService.save(diff, InputType.LEFT));
    }

    /**
     * Encodes the request body and set it as right attribute from {@link Diff}.
     *
     * @param diffId diff identifier.
     * @return {@link DiffDto}.
     */
    @PostMapping(path = "diff/{diffId}/right", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Saves the request body encoded in base64 as right attribute.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = DiffDto.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 415, message = "Unsupported Media Type", response = ErrorResponseDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    public DiffDto saveRight(
        @ApiParam(name = DIFF_ID, required = true)
        @PathVariable String diffId,
        @ApiParam(name = "right", required = true)
        @Valid @RequestBody Base64Dto base64Dto) {

        log.info("Received {} for {}", kv("right", base64Dto.getBase64Value()), kv(DIFF_ID, diffId));

        final Diff diff = diffMapper.base64DtoToRightDiff(base64Dto);
        diff.setDiffId(diffId);

        return diffMapper.diffToDiffDto(diffService.save(diff, InputType.RIGHT));
    }

    /**
     * Compares the left and right inputs of the given diffId.
     *
     * @param diffId diff identifier.
     * @return {@link DiffDto}.
     */
    @GetMapping(path = "diff/{diffId}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Compares the left and right inputs of the given diffId.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = DiffDto.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorResponseDto.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDto.class)
    })
    public DiffDto diff(
        @ApiParam(name = DIFF_ID, required = true)
        @PathVariable String diffId) {

        log.info("Comparing {}", kv(DIFF_ID, diffId));

        return diffService.diff(diffService.findById(diffId));
    }
}
