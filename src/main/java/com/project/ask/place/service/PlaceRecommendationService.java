package com.project.ask.place.service;

import com.project.ask.api.dto.DocumentDto;
import com.project.ask.api.dto.KakaoApiResponseDto;
import com.project.ask.api.service.KakaoAddressSearchService;
import com.project.ask.direction.dto.OutputDto;
import com.project.ask.direction.entity.Direction;
import com.project.ask.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    public List<OutputDto> recommendPlaceList(String address) {

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        // validation check
        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PlaceRecommendationService recommendPlaceList fail] Input address: {}", address);
            return Collections.emptyList();
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        List<Direction> directionList = directionService.buildDirectionList(documentDto); //공공기관 데이터 사용
//        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto); //카카오 카테고리 주소 검색 api 사용

        return directionService.saveAll(directionList)
                .stream()
                .map(t -> convertToOutputDto(t))
                .collect(Collectors.toList());

    }

    private OutputDto convertToOutputDto(Direction direction) {
        return OutputDto.builder()
                .placeAddress(direction.getTargetAddress())
                .placeName(direction.getTargetPlaceName())
                .directionUrl("todo") //todo
                .roadViewUrl("todo")
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
