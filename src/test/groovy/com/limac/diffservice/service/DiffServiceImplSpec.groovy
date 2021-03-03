package com.limac.diffservice.service

import com.limac.diffservice.domain.Diff
import com.limac.diffservice.exception.NotFoundException
import com.limac.diffservice.rest.dto.DiffDto
import com.limac.diffservice.rest.dto.DiffResultDto
import com.limac.diffservice.exception.MissingInputException
import com.limac.diffservice.repository.DiffRepository
import com.limac.diffservice.type.InputType
import com.limac.diffservice.type.ResultType
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets

class DiffServiceImplSpec extends Specification {

    DiffRepository diffRepository = Mock(DiffRepository)

    DiffService diffService = new DiffServiceImpl(diffRepository)

    def 'non existing diffId should be saved in the repository'() {
        given: 'a non existing diffId to be saved'
        Diff newDiff = new Diff()
        newDiff.diffId = 'newDiffId'

        1 * diffRepository.findById(newDiff.diffId) >> Optional.empty()
        1 * diffRepository.save(newDiff) >> newDiff

        when: 'save is executed'
        Diff diffSaved = diffService.save(newDiff, InputType.LEFT)

        then: 'newDiff is saved in the repository'
        noExceptionThrown()
        diffSaved == newDiff
    }

    @Unroll('if the diffId already exists the Diff updated should be [leftInput: #expectedLeftInput, rightInput: #expectedRightInput] accordingly with the input type #inputType')
    def 'if the diffId already exists the Diff input should be updated accordingly with the input type'() {
        given: 'an existing diffId'
        Diff existingDiff = new Diff()
        existingDiff.diffId = 'existingDiffId'
        existingDiff.left = existingLeftInput
        existingDiff.right = existingRightInput
        Optional<Diff> optionalExistingDiff = Optional.of(existingDiff)

        Diff newDiff = new Diff()
        newDiff.diffId = 'existingDiffId'
        newDiff.left = newLeftInput
        newDiff.right = newRightInput

        1 * diffRepository.findById(newDiff.diffId) >> optionalExistingDiff
        1 * diffRepository.save(existingDiff) >> optionalExistingDiff.get()

        when: 'save is called with a newDiff'
        Diff diffSaved = diffService.save(newDiff, inputType)

        then: 'left and right inputs should be updated accordingly with the inputType'
        noExceptionThrown()
        diffSaved.left == expectedLeftInput
        diffSaved.right == expectedRightInput

        where:
        inputType       | existingLeftInput     | existingRightInput    | newLeftInput      | newRightInput     | expectedLeftInput     | expectedRightInput
        InputType.LEFT  | 'existingLeftInput'   | 'existingRightInput'  | null              | 'newRightInput'   | null                  | 'existingRightInput'
        InputType.LEFT  | 'existingLeftInput'   | 'existingRightInput'  | 'newLeftInput'    | null              | 'newLeftInput'        | 'existingRightInput'
        InputType.LEFT  | null                  | 'existingRightInput'  | null              | 'newRightInput'   | null                  | 'existingRightInput'
        InputType.LEFT  | null                  | 'existingRightInput'  | 'newLeftInput'    | null              | 'newLeftInput'        | 'existingRightInput'
        InputType.LEFT  | 'existingLeftInput'   | null                  | null              | 'newRightInput'   | null                  | null
        InputType.LEFT  | 'existingLeftInput'   | null                  | 'newLeftInput'    | null              | 'newLeftInput'        | null
        InputType.RIGHT | 'existingLeftInput'   | 'existingRightInput'  | null              | 'newRightInput'   | 'existingLeftInput'   | 'newRightInput'
        InputType.RIGHT | 'existingLeftInput'   | 'existingRightInput'  | 'newLeftInput'    | null              | 'existingLeftInput'   | null
        InputType.RIGHT | null                  | 'existingRightInput'  | null              | 'newRightInput'   | null                  | 'newRightInput'
        InputType.RIGHT | null                  | 'existingRightInput'  | 'newLeftInput'    | null              | null                  | null
        InputType.RIGHT | 'existingLeftInput'   | null                  | null              | 'newRightInput'   | 'existingLeftInput'   | 'newRightInput'
        InputType.RIGHT | 'existingLeftInput'   | null                  | 'newLeftInput'    | null              | 'existingLeftInput'   | null
    }

    def 'findById should return the Diff if the diffId is found'() {
        given: 'an existing diffId'
        String existingDiffId = 'existingDiffId'

        Diff diff = new Diff()
        diff.diffId = existingDiffId

        1 * diffRepository.findById(existingDiffId) >> Optional.of(diff)

        when: 'findById is executed'
        Diff diffFound = diffService.findById(existingDiffId)

        then: 'Diff is found'
        diff == diffFound
    }

    def 'findById should throw NotFoundException if the diffId does not exists'() {
        given: 'a non existing diffId'
        String nonExistingDiffId = 'nonExistingDiffId'

        1 * diffRepository.findById(nonExistingDiffId) >> Optional.empty()

        when: 'findById is executed'
        diffService.findById(nonExistingDiffId)

        then: 'NotFoundException is thrown'
        NotFoundException notFoundException = thrown(NotFoundException)
        notFoundException.message == "could not find diffId: ${nonExistingDiffId}"
    }

    @Unroll('diff should be able define the result #resultType ')
    def 'diff should compare the left and right inputs and return the result type'() {
        given: 'a Diff'
        Diff diff = new Diff()
        diff.diffId = 'diffId'

        diff.left = new String(Base64.encoder.encode(leftInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        diff.right = new String(Base64.encoder.encode(rightInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)

        when: 'diff is executed'
        DiffDto diffDto = diffService.diff(diff)

        then: 'DiffDto should be returned with the resultType expected'
        noExceptionThrown()
        diffDto.resultType == resultType

        where:
        leftInput               | rightInput                            | resultType
        '{"property":"value"}'  | '{"property":"value"}'                | ResultType.EQUAL
        '{"property":"value"}'  | '{"anotherProperty":"anotherValue"}'  | ResultType.DIFFERENT_SIZE
        '{"sameSize":"value"}'  | '{"property":"value"}'                | ResultType.SAME_SIZE_BUT_DIFFERENT
    }

    @SuppressWarnings('LineLength')
    def 'diff should compare the left and right inputs and return the diff results'() {
        given: 'a Diff'
        Diff diff = new Diff()
        diff.diffId = 'diffId'

        diff.left = new String(Base64.encoder.encode(leftInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        diff.right = new String(Base64.encoder.encode(rightInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)

        when: 'diff is executed'
        DiffDto diffDto = diffService.diff(diff)

        then: 'DiffDto should be returned with the resultType expected'
        noExceptionThrown()
        diffDto.resultType == resultType
        diffDto.diffResults == diffResults

        where:
        leftInput               | rightInput                | resultType                            | diffResults
        '{"sameSize":"value"}'  | '{"property":"false"}'    | ResultType.SAME_SIZE_BUT_DIFFERENT    | Arrays.asList(DiffResultDto.builder().offset(3).length(10).build(), DiffResultDto.builder().offset(17).length(1).build(), DiffResultDto.builder().offset(22).length(1).build())
    }

    def 'diff should throw MissingInputException if any input is missing'() {
        given: 'a Diff'
        Diff diff = new Diff()
        diff.diffId = 'diffId'

        diff.left = leftInput
        diff.right = rightInput

        when: 'diff is executed'
        diffService.diff(diff)

        then: 'MissingInputException is thrown'
        MissingInputException missingInputException = thrown(MissingInputException)
        missingInputException.message == "left and/or right input is missing for diffId: ${diff.diffId}"

        where:
        leftInput               | rightInput
        '{"property":"value"}'  | null
        '{"property":"value"}'  | ''
        null                    | '{"property":"value"}'
        ''                      | '{"property":"value"}'
        null                    | null
        null                    | ''
        ''                      | ''
        ''                      | null
    }
}
