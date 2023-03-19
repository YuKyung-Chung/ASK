package com.project.ask.direction.service

import spock.lang.Specification

//인코딩 된 값이 디코딩 되었을 때 동일한 값이 나오는지 확인하는 단위 테스트
class Base62ServiceTest extends Specification {

    private Base62Service base62Service

    def setup() {
        base62Service = new Base62Service()
    }

    def "check base62 encoder/decoder"() {
        given:
        long num = 5

        when:
        def encodedId = base62Service.encodeDirectionId(num)

        def decodedId = base62Service.decodeDirectionId(encodedId)

        then:
        num == decodedId
    }
}
