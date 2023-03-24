package com.project.ask.place.service

import com.project.ask.place.cache.PlaceRedisTemplateService
import com.project.ask.place.entity.Place
import org.assertj.core.util.Lists
import spock.lang.Specification

class PlaceSearchServiceTest extends Specification {

    private PlaceSearchService placeSearchService

    private PlaceRepositoryService placeRepositoryService = Mock()
    private PlaceRedisTemplateService placeRedisTemplateService = Mock()

    private List<Place> placeList

    def setup() {
        placeSearchService = new PlaceSearchService(placeRepositoryService, placeRedisTemplateService)

        placeList = Lists.newArrayList(
                Place.builder()
                        .id(1L)
                        .placeName("어답산관광지")
                        .latitude(37.59380861)
                        .longitude(128.0575207)
                        .build(),
                Place.builder()
                        .id(2L)
                        .placeName("송도오션파크")
                        .latitude(35.0727183)
                        .longitude(129.0174477)
                        .build())
    }

    def "redis 장애시 DB를 이용하여 약국 데이터 조회"() {
        when:
        placeRedisTemplateService.findAll() >> [] //빈 리스트 호출되도록 stubbing
        placeRepositoryService.findAll() >> placeList

        def result = placeSearchService.searchPlaceDtoList()

        then:
        result.size() == 2
    }
}
