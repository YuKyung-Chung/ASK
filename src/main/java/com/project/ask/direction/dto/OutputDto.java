package com.project.ask.direction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutputDto {

    private String placeName; // 관광명소 명
    private String placeAddress; // 관광지 주소
    private String directionUrl; // 길안내 url
    private String roadViewUrl; // 로드뷰 url
    private String distance; // 고객 주소와 관광지 주소의 거리
}
