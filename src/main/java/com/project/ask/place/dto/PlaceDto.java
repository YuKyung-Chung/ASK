package com.project.ask.place.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto {
    private Long id;
    private String placeName;
    private String placeAddress;
    private double latitude;
    private double longitude;
}
