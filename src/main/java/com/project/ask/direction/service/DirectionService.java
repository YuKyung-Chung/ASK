package com.project.ask.direction.service;

import com.project.ask.api.dto.DocumentDto;
import com.project.ask.direction.entity.Direction;
import com.project.ask.place.dto.PlaceDto;
import com.project.ask.place.service.PlaceSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT = 3; // 최대 검색 개수
    private static final double RADIUS_KM = 10.0; // 반경 10 km

    private final PlaceSearchService placeSearchService;

    public List<Direction> buildDirectionList(DocumentDto documentDto) {

        if(Objects.isNull(documentDto)) return Collections.emptyList();

        // 관광지 데이터 조회
        // 거리 계산 알고리즘 이용하여 고객과 관광지 사이 거리 계산 후 sort
        return placeSearchService.searchPlaceDtoList()
                .stream().map(placeDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetPlaceName(placeDto.getPlaceName())
                                .targetAddress(placeDto.getPlaceAddress())
                                .targetLongitude(placeDto.getLongitude())
                                .targetLatitude(placeDto.getLatitude())
                                .distance(
                                        calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(),
                                                placeDto.getLatitude(), placeDto.getLongitude())
                                )
                                .build())
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());

    }

    //Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}
