package com.limac.diffservice.service;

import com.limac.diffservice.domain.Diff;
import com.limac.diffservice.exception.MissingInputException;
import com.limac.diffservice.exception.NotFoundException;
import com.limac.diffservice.repository.DiffRepository;
import com.limac.diffservice.rest.dto.DiffDto;
import com.limac.diffservice.rest.dto.DiffResultDto;
import com.limac.diffservice.type.InputType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.limac.diffservice.type.ResultType.*;
import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * {@link DiffService} implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiffServiceImpl implements DiffService {

    private static final String DIFF_ID = "diffId";

    private final DiffRepository diffRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Diff save(Diff diff, InputType inputType) {
        return diffRepository.findById(diff.getDiffId())
            .map(diffFound -> {
                if (inputType == InputType.LEFT) {
                    diffFound.setLeft(diff.getLeft());
                } else {
                    diffFound.setRight(diff.getRight());
                }

                log.info("Updating Diff {}", kv(DIFF_ID, diffFound.getDiffId()));

                return diffRepository.save(diffFound);
            })
            .orElseGet(() -> {

                log.info("Adding Diff {}", kv(DIFF_ID, diff.getDiffId()));

                return diffRepository.save(diff);
            });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Diff findById(String diffId) {
        return diffRepository.findById(diffId).orElseThrow(() -> new NotFoundException("could not find diffId: " + diffId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiffDto diff(Diff diff) {
        log.info("Executing diff for {}", kv(DIFF_ID, diff.getDiffId()));

        final String diffId = diff.getDiffId();
        final String left = diff.getLeft();
        final String right = diff.getRight();

        if (StringUtils.isBlank(left) || StringUtils.isBlank(right)) {
            throw new MissingInputException("left and/or right input is missing for diffId: " + diffId);
        }

        final DiffDto.DiffDtoBuilder diffDtoBuilder = DiffDto.builder();

        diffDtoBuilder.diffId(diffId);
        diffDtoBuilder.left(left);
        diffDtoBuilder.right(right);

        if (left.equals(right)) {
            diffDtoBuilder.resultType(EQUAL);

        } else if (left.length() != right.length()) {
            diffDtoBuilder.resultType(DIFFERENT_SIZE);

        } else {
            diffDtoBuilder.resultType(SAME_SIZE_BUT_DIFFERENT);
            diffDtoBuilder.diffResults(compare(left, right));
        }

        final DiffDto diffDto = diffDtoBuilder.build();

        log.info("Diff executed: {}, {}, {}, {}, {}",
            kv(DIFF_ID, diffDto.getDiffId()), kv("left", diffDto.getLeft()), kv("right", diffDto.getRight()),
            kv("resultType", diffDto.getResultType()), kv("diffResults", diffDto.getDiffResults()));

        return diffDto;
    }

    private List<DiffResultDto> compare(String left, String right) {
        final List<DiffResultDto> diffResults = new ArrayList<>();
        final DiffResultDto.DiffResultDtoBuilder diffResultDtoBuilder = DiffResultDto.builder();

        int length = 0;
        int offset = -1;

        for (int i = 0; i <= left.length(); i++) {

            if (i < left.length() && left.charAt(i) != right.charAt(i)) {

                length++;

                if (offset < 0) {
                    offset = i;
                }

            } else if (offset != -1) {

                diffResultDtoBuilder.offset(offset);
                diffResultDtoBuilder.length(length);

                diffResults.add(diffResultDtoBuilder.build());

                length = 0;
                offset = -1;
            }
        }

        return diffResults;
    }
}
