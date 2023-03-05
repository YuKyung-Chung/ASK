package com.project.ask.place.service;

import com.project.ask.place.dto.PlaceDto;
import com.project.ask.place.entity.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceSearchService {

    private final PlaceRepositoryService placeRepositoryService;

    public List<PlaceDto> searchPlaceDtoList() {

        //redis

        //db
        return placeRepositoryService.findAll()
                .stream()
                .map(entity -> convertToPlaceDto(entity))
                .collect(Collectors.toList());
    }

    private PlaceDto convertToPlaceDto(Place place) {
        return PlaceDto.builder()
                .id(place.getId())
                .placeAddress(place.getPlaceAddress())
                .placeName(place.getPlaceName())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
    }
}
