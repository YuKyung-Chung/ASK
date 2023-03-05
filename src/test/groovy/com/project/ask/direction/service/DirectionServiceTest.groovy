package com.project.ask.direction.service

import com.project.ask.api.dto.DocumentDto
import com.project.ask.place.dto.PlaceDto
import com.project.ask.place.service.PlaceSearchService
import spock.lang.Specification

class DirectionServiceTest extends Specification {

    private PlaceSearchService placeSearchService = Mock()

    private DirectionService directionService = new DirectionService(placeSearchService)

    private List<PlaceDto> placeList

    def setup() {
        placeList = new ArrayList<>()
        placeList.addAll(
                PlaceDto.builder()
                        .id(1L)
                .placeName("진남관")
                        .placeAddress("주소1")
                        .latitude(34.7413383)
                        .longitude(127.7365704)
                        .build(),
                PlaceDto.builder()
                        .id(2L)
                        .placeName("오동도")
                        .placeAddress("주소2")
                        .latitude(34.7443435)
                        .longitude(127.764121)
                        .build()
        )
    }

    def "buildDirectionList - 결과 값이 거리순으로 정렬 되는지 확인"() {
        given:
        def addressName = "전라남도 여수시 자산4길 39"
        double inputLatitude = 34.7402223
        double inputLongitude = 127.7529197

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        placeSearchService.searchPlaceDtoList() >> placeList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetPlaceName == "오동도"
        results.get(1).targetPlaceName == "진남관"
    }

    def "buildDirectionList - 정해진 반경 10 km 내에 검색이 되는지 확인"() {
        given:
        def addressName = "전라남도 여수시 자산4길 39"
        double inputLatitude = 34.7402223
        double inputLongitude = 127.7529197

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        placeSearchService.searchPlaceDtoList() >> placeList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetPlaceName == "오동도"
        results.get(1).targetPlaceName == "진남관"
    }
}
