package com.project.ask.place.service;

import com.project.ask.api.dto.DocumentDto;
import com.project.ask.api.dto.KakaoApiResponseDto;
import com.project.ask.api.service.KakaoAddressSearchService;
import com.project.ask.direction.entity.Direction;
import com.project.ask.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    public void recommendPlaceList(String address) {

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        // validation check
        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PlaceRecommendationService recommendPlaceList fail] Input address: {}", address);
            return;
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

//        List<Direction> directionList = directionService.buildDirectionList(documentDto); //공공기관 데이터 사용
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto); //카카오 카테고리 주소 검색 api 사용

        directionService.saveAll(directionList);

    }
}
