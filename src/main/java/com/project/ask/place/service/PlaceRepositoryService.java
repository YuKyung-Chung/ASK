package com.project.ask.place.service;

import com.project.ask.place.entity.Place;
import com.project.ask.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceRepositoryService {
    private final PlaceRepository placeRepository;

    @Transactional
    public void updateAddress(Long id, String address) {
        Place entity = placeRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error("[PlaceRepositoryService updateAddress] not found id: {}",id);
            return;
        }

        entity.changePlaceAddress(address);
    }

    //for test(JPA dirty checking)
    public void updateAddressWithoutTransaction(Long id, String address) {
        Place entity = placeRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error("[PlaceRepositoryService updateAddress] not found id: {}",id);
            return;
        }

        entity.changePlaceAddress(address);
    }
}
