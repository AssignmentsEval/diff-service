package com.limac.diffservice.rest

import com.limac.diffservice.domain.Diff
import com.limac.diffservice.mapping.DiffMapper
import com.limac.diffservice.rest.dto.DiffDto
import com.limac.diffservice.rest.dto.Base64Dto
import com.limac.diffservice.service.DiffService
import com.limac.diffservice.type.InputType
import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static com.limac.diffservice.type.ResultType.EQUAL

class DiffControllerSpec extends Specification {

    DiffService diffService = Mock(DiffService)
    DiffMapper diffMapper = Mock(DiffMapper)

    DiffController diffController = new DiffController(diffService, diffMapper)

    def 'saveLeft API should save the left input of a Diff'() {
        given: 'a diffId and an input to be saved'
        String diffId = 'diffId'
        String leftInput = '{"property":"value"}'
        String leftEncoded = new String(Base64.encoder.encode(leftInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)

        Base64Dto base64Dto = new Base64Dto()
        base64Dto.base64Value = leftEncoded

        Diff diff = new Diff()
        diff.diffId = diffId
        diff.left = leftEncoded

        DiffDto diffDto = new DiffDto.DiffDtoBuilder().diffId(diffId).left(leftEncoded).build()

        1 * diffMapper.base64DtoToLeftDiff(base64Dto) >> diff
        1 * diffService.save(diff, InputType.LEFT) >> diff
        1 * diffMapper.diffToDiffDto(diff) >> diffDto

        when: 'saveLeft is executed'
        DiffDto diffDtoReturned = diffController.saveLeft(diffId, base64Dto)

        then: 'Diff with the left input should be saved'
        noExceptionThrown()
        diffDtoReturned.diffId == diffId
        diffDtoReturned.left == leftEncoded
    }

    def 'saveRight API should save the right input of a Diff'() {
        given: 'a diffId and an input to be saved'
        String diffId = 'diffId'
        String rightInput = '{"property":"value"}'
        String rightEncoded = new String(Base64.encoder.encode(rightInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)

        Base64Dto base64Dto = new Base64Dto()
        base64Dto.base64Value = rightEncoded

        Diff diff = new Diff()
        diff.diffId = diffId
        diff.right = rightEncoded

        DiffDto diffDto = new DiffDto.DiffDtoBuilder().diffId(diffId).right(rightEncoded).build()

        1 * diffMapper.base64DtoToRightDiff(base64Dto) >> diff
        1 * diffService.save(diff, InputType.RIGHT) >> diff
        1 * diffMapper.diffToDiffDto(diff) >> diffDto

        when: 'saveRight is executed'
        DiffDto diffDtoReturned = diffController.saveRight(diffId, base64Dto)

        then: 'Diff with the right input should be saved'
        noExceptionThrown()
        diffDtoReturned.diffId == diffId
        diffDtoReturned.right == rightEncoded
    }

    def 'diff API should call the service to compare the left and right inputs of the given diffId'() {
        given: 'a diffId to be compared'
        String diffId = 'diffId'

        String input = '{"property":"value"}'
        String inputEncoded = new String(Base64.encoder.encode(input.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)

        Diff diff = new Diff()
        diff.diffId = diffId
        diff.left = inputEncoded
        diff.right = inputEncoded

        DiffDto diffDto = new DiffDto.DiffDtoBuilder().diffId(diffId).left(inputEncoded).right(inputEncoded).resultType(EQUAL).build()

        1 * diffService.findById(diffId) >> diff
        1 * diffService.diff(diff) >> diffDto

        when: 'diff is executed'
        DiffDto diffDtoReturned = diffController.diff(diffId)

        then: 'service to compare left and right inputs should be called and an instance of DiffDto should be returned containing the results'
        noExceptionThrown()
        diffDtoReturned == diffDto
    }
}
