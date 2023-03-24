package com.project.ask.place.cache

import com.project.ask.AbstractIntegrationContainerBaseTest
import com.project.ask.place.dto.PlaceDto
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class PlaceRedisTemplateServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private PlaceRedisTemplateService placeRedisTemplateService

    //각각 테스트마다 영향받지 않도록 비워주는 작업
    def setup() {
        placeRedisTemplateService.findAll()
                .forEach(dto-> {
                    placeRedisTemplateService.delete(dto.getId())
                })
    }

    def "save success"() {
        given:
        String placeName = "name"
        String placeAddress = "address"
        PlaceDto dto = PlaceDto.builder()
                .id(1L)
                .placeName(placeName)
                .placeAddress(placeAddress)
                .build()

        when:
        placeRedisTemplateService.save(dto)
        List<PlaceDto> result = placeRedisTemplateService.findAll()

        then:
        result.size() == 1
        result.get(0).id == 1L
        result.get(0).placeName == placeName
        result.get(0).placeAddress == placeAddress
    }

    def "success fail"() {
        given:
        PlaceDto dto =
                PlaceDto.builder()
                        .build()

        when:
        placeRedisTemplateService.save(dto)
        List<PlaceDto> result = placeRedisTemplateService.findAll()

        then:
        result.size() == 0
    }

    def "delete"() {
        given:
        String placeName = "name"
        String placeAddress = "address"
        PlaceDto dto =
                PlaceDto.builder()
                        .id(1L)
                        .placeName(placeName)
                        .placeAddress(placeAddress)
                        .build()

        when:
        placeRedisTemplateService.save(dto)
        placeRedisTemplateService.delete(dto.getId())
        def result = placeRedisTemplateService.findAll()

        then:
        result.size() == 0
    }
}
