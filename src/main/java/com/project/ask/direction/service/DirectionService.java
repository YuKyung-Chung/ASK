package com.project.ask.direction.service;

import com.project.ask.api.dto.DocumentDto;
import com.project.ask.api.service.KakaoCategorySearchService;
import com.project.ask.direction.entity.Direction;
import com.project.ask.direction.repository.DirectionRepository;
import com.project.ask.place.dto.PlaceDto;
import com.project.ask.place.service.PlaceSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT = 3; // 최대 검색 개수
    private static final double RADIUS_KM = 10.0; // 반경 10 km
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    private final PlaceSearchService placeSearchService;
    private final DirectionRepository directionRepository;
    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final Base62Service base62Service;

    @Transactional //데이터 변경이 있으므로 트랜잭션 처리
    public List<Direction> saveAll(List<Direction> directionList) {
        // validation check
        if(CollectionUtils.isEmpty(directionList)) return Collections.emptyList();
        return directionRepository.saveAll(directionList);
    }

    public String findDirectionUrlById(String encodedId) {
        Long decodedId = base62Service.decodeDirectionId(encodedId);
        Direction direction = directionRepository.findById(decodedId).orElse(null);

        String params = String.join(",", direction.getTargetPlaceName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();
        return result;
    }

    // 공공데이터 기반으로 고객이 입력한 주소와 비교하여 가까운 장소 검색하는 메서드
    public List<Direction> buildDirectionList(DocumentDto documentDto) {

        // validation check
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

    // category kakao api로 장소 검색하는 메서드 (공공데이터 사용 X)
    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {
        if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return kakaoCategorySearchService
                .requestPlaceCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .getDocumentList()
                .stream().map(resultDocumentDto ->
                        Direction.builder()
                                .inputAddress(inputDocumentDto.getAddressName())
                                .inputLatitude(inputDocumentDto.getLatitude())
                                .inputLongitude(inputDocumentDto.getLongitude())
                                .targetPlaceName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001) // km 단위로 변경
                                .build())
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
