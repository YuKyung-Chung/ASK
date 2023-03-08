package com.project.ask.direction.controller

import com.project.ask.direction.dto.OutputDto
import com.project.ask.place.service.PlaceRecommendationService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

class FormControllerTest extends Specification {

    private MockMvc mockMvc
    private PlaceRecommendationService placeRecommendationService = Mock()
    private List<OutputDto> outputDtoList //응답객체

    def setup() {
        // FormController MockMvc 객체로 만든다
        mockMvc = MockMvcBuilders.standaloneSetup(new FormController(placeRecommendationService))
                .build()

        outputDtoList = new ArrayList<>()
        outputDtoList.addAll(
                OutputDto.builder()
                        .placeName("place1")
                        .build(),
                OutputDto.builder()
                        .placeName("place2")
                        .build()

        )
    }

    def "GET /"() {
        expect:
        // FormComtroller의 "/" URI를 get 방식으로 호출
        mockMvc.perform(get("/"))
                .andExpect(handler().handlerType(FormController.class))
                .andExpect(handler().methodName("main"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andDo(log())
    }

    def "POST /search"() {

        given:
        String inputAddress = "서울 성북구 종암동"

        when:
        def resultActions = mockMvc.perform(post("/search")
                .param("address", inputAddress))

        then:
        1 * placeRecommendationService.recommendPlaceList(argument -> {
            assert argument == inputAddress // mock 객체의 argument 검증
        }) >> outputDtoList // 응답값을 리턴한다

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("output"))
                .andExpect(model().attributeExists("outputFormList")) // outputFormList라는 key가 model에 있는지 검증
                .andExpect(model().attribute("outputFormList", outputDtoList)) // outputFormList 에 해당하는 데이터가 outputDtoList 객체인지 검증
                .andDo(print())
    }
}

