package com.project.ask.direction.entity;


import com.project.ask.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "direction")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Direction extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 고객의 주소정보
    private String inputAddress;
    private double inputLatitude;
    private double inputLongitude;

    // 관광지 정보
    private String targetPlaceName;
    private String targetAddress;
    private double targetLatitude;
    private double targetLongitude;

    // 고객 주소 와 관광지 주소 사이의 거리
    private double distance;
}