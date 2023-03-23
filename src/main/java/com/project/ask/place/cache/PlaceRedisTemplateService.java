package com.project.ask.place.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ask.place.dto.PlaceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceRedisTemplateService {

    private static final String CACHE_KEY = "PLACE";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper; //하나의 singleton으로 구성하여 빈으로 등록

    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(PlaceDto placeDto) {
        if(Objects.isNull(placeDto) || Objects.isNull(placeDto.getId())) {
            log.error("Required Values must not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    placeDto.getId().toString(),
                    serializePlaceDto(placeDto));
            log.info("[PlaceRedisTemplateService save success] id: {}", placeDto.getId());
        } catch (Exception e) {
            log.error("[PlaceRedisTemplateService save error] {}", e.getMessage());
        }
    }

    public List<PlaceDto> findAll() {

        try {
            List<PlaceDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                PlaceDto placeDto = deserializePlaceDto(value);
                list.add(placeDto);
            }
            return list;

        } catch (Exception e) {
            log.error("[PlaceRedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[PlaceRedisTemplateService delete]: {} ", id);
    }

    private String serializePlaceDto(PlaceDto placeDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(placeDto);
    }

    private PlaceDto deserializePlaceDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, PlaceDto.class);
    }
}
