package com.project.ask.place.repository

import com.project.ask.AbstractIntegrationContainerBaseTest
import com.project.ask.place.entity.Place
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest
class PlaceRepositoryTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private PlaceRepository placeRepository

    //시작 전 실행되는 메서드
    def setup() {
        placeRepository.deleteAll()
    }

    def "PlaceRepository save"(){
        given:
        String placeName = "무릉계곡";
        String placeAddress = "강원도 동해시 삼화로 584";
        double latitude = 37.463551;
        double longitude = 129.014511;

        def place = Place.builder()
                .placeAddress(placeAddress)
                .placeName(placeName)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def result = placeRepository.save(place)

        then:
        result.getPlaceAddress() == placeAddress
        result.getPlaceName() == placeName
        result.getLatitude() == latitude
        result.getLongitude() == longitude
    }

    def "PlaceRepository saveAll"(){
        given:
        String placeName = "무릉계곡";
        String placeAddress = "강원도 동해시 삼화로 584";
        double latitude = 37.463551;
        double longitude = 129.014511;

        def place = Place.builder()
                .placeAddress(placeAddress)
                .placeName(placeName)
                .latitude(latitude)
                .longitude(longitude)
                .build()
        when:
        placeRepository.saveAll(Arrays.asList(place))
        def result = placeRepository.findAll()

        then:
        result.size() == 1
    }

    def "Auditable 등록"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        String address = "강원도 동해시 삼화로 584"
        String name = "무릉계곡"

        def place = Place.builder()
                .placeAddress(address)
                .placeName(name)
                .build()

        when:
        placeRepository.save(place)
        def result = placeRepository.findAll()

        then:
        result.get(0).getCreatedDate().isAfter(now)
        result.get(0).getModifiedDate().isAfter(now)
    }
}

