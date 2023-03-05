package com.project.ask.place.entity;

import com.project.ask.Auditable;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "place")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeName;
    private String placeAddress;
    private double latitude;
    private double longitude;

    public void changePlaceAddress(String address) {
        this.placeAddress = address;
    }
}
