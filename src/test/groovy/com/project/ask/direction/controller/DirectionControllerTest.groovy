package com.project.ask.direction.controller

import com.project.ask.direction.service.DirectionService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

//Controller 단위 테스트
class DirectionControllerTest extends Specification {

    private MockMvc mockMvc
    private DirectionService directionService = Mock() //DirectionService mocking

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DirectionController(directionService))
                .build()
    }

    def "GET /dir/{encodedId}"() {
        given:
        String encodedId = "r"

        String redirectURL = "https://map.kakao.com/link/map/place,38.11,128.11"

        when:
        //encodedId 입력 받으면 redirectUrl return
        directionService.findDirectionUrlById(encodedId) >> redirectURL

        def result = mockMvc.perform(MockMvcRequestBuilders.get("/dir/{encodedId}", encodedId))

        then:
        result.andExpect(status().is3xxRedirection())  //리다이렉트 발생 확인
                .andExpect(redirectedUrl(redirectURL)) //리다이렉트 경로 검증
                .andDo(print())

    }
}
