package com.project.ask.place.service

import com.project.ask.AbstractIntegrationContainerBaseTest
import com.project.ask.place.entity.Place
import com.project.ask.place.repository.PlaceRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class PlaceRepositoryServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    PlaceRepositoryService placeRepositoryService

    @Autowired
    private PlaceRepository placeRepository

    def setup() {
        placeRepository.deleteAll()
    }

    def "PlaceRepository update - dirty checking success"() {
        given:
        String inputAddress = "서울 특별시 성북구 종암동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = ""

        def place = Place.builder()
                .placeAddress(inputAddress)
                .placeName(name)
                .build()

        when:
        def entity = placeRepository.save(place)
        placeRepositoryService.updateAddress(entity.getId(), modifiedAddress)

        def result = placeRepository.findAll()

        then:
        result.get(0).getPlaceAddress() == modifiedAddress
    }

    def "PlaceRepository update - dirty checking fail"() {
        given:
        String inputAddress = "서울 특별시 성북구 종암동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = ""

        def place = Place.builder()
                .placeAddress(inputAddress)
                .placeName(name)
                .build()

        when:
        def entity = placeRepository.save(place)
        placeRepositoryService.updateAddressWithoutTransaction(entity.getId(), modifiedAddress)

        def result = placeRepository.findAll()

        then:
        result.get(0).getPlaceAddress() == inputAddress
    }


}
