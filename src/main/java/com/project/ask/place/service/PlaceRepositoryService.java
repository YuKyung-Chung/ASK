package com.project.ask.place.service;

import com.project.ask.place.entity.Place;
import com.project.ask.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
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
            log.error("[PlaceRepositoryService updateAddress] not found id: {}", id);
            return;
        }

        entity.changePlaceAddress(address);
    }

    //for test(JPA dirty checking)
    public void updateAddressWithoutTransaction(Long id, String address) {
        Place entity = placeRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error("[PlaceRepositoryService updateAddress] not found id: {}", id);
            return;
        }

        entity.changePlaceAddress(address);
    }

    @Transactional(readOnly = true) // readOnly 설정하면 dirty checking 진행되지 않아 약간 성능 향상됨
    public List<Place> findAll() {
        return placeRepository.findAll();
    }

}
