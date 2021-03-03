package com.limac.diffservice.mapping

import com.limac.diffservice.domain.Diff
import com.limac.diffservice.rest.dto.DiffDto
import com.limac.diffservice.rest.dto.Base64Dto
import spock.lang.Specification

class DiffMapperImplSpec extends Specification {

    DiffMapper diffMapper = new DiffMapperImpl()

    def 'Base64Dto mapping to left input of Diff'() {
        given: 'an Base64Dto'
        Base64Dto base64Dto = new Base64Dto()
        base64Dto.base64Value = 'base64Value'

        when: 'Base64Dto is mapped to Diff'
        Diff diff = diffMapper.base64DtoToLeftDiff(base64Dto)

        then: 'Diff should be mapped correctly'
        diff.left == base64Dto.base64Value
    }

    def 'null mapping to left input of Diff'() {
        when: 'null is mapped to Diff'
        Diff diff = diffMapper.base64DtoToLeftDiff(null)

        then: 'mapping result should be null'
        diff == null
    }

    def 'Base64Dto mapping to right input of Diff'() {
        given: 'an Base64Dto'
        Base64Dto base64Dto = new Base64Dto()
        base64Dto.base64Value = 'base64Value'

        when: 'Base64Dto is mapped to Diff'
        Diff diff = diffMapper.base64DtoToRightDiff(base64Dto)

        then: 'Diff should be mapped correctly'
        diff.right == base64Dto.base64Value
    }

    def 'null mapping to right input of Diff'() {
        when: 'null is mapped to Diff'
        Diff diff = diffMapper.base64DtoToRightDiff(null)

        then: 'mapping result should be null'
        diff == null
    }

    def 'Diff mapping to DiffDto'() {
        given: 'a Diff'
        Diff diff = new Diff()
        diff.diffId = 'diffId'
        diff.left = 'left'
        diff.right = 'right'

        when: 'Diff is mapped to DiffDto'
        DiffDto diffDto = diffMapper.diffToDiffDto(diff)

        then: 'DiffDto should be mapped correctly'
        diffDto.diffId == diff.diffId
        diffDto.left == diff.left
        diffDto.right == diff.right
    }

    def 'null mapping to DiffDto'() {
        when: 'null is mapped to DiffDto'
        DiffDto diffDto = diffMapper.diffToDiffDto(null)

        then: 'mapping result should be null'
        diffDto == null
    }
}
