package com.project.ask.place.controller;

import com.project.ask.place.cache.PlaceRedisTemplateService;
import com.project.ask.place.dto.PlaceDto;
import com.project.ask.place.service.PlaceRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceRepositoryService placeRepositoryService;
    private final PlaceRedisTemplateService placeRedisTemplateService;

    // 데이터 초기 셋팅을 위한 임시 메서드
    @GetMapping("/redis/save")
    public String save() {
        List<PlaceDto> placeDtoList = placeRepositoryService.findAll()
                .stream().map(place -> PlaceDto.builder()
                        .id(place.getId())
                        .placeName(place.getPlaceName())
                        .placeAddress(place.getPlaceAddress())
                        .latitude(place.getLatitude())
                        .longitude(place.getLongitude())
                        .build())
                .collect(Collectors.toList());

        placeDtoList.forEach(placeRedisTemplateService::save);


        return "success";
    }
}
