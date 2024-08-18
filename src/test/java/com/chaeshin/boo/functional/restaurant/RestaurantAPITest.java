package com.chaeshin.boo.functional.restaurant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chaeshin.boo.service.member.auth.JwtAuthService;
import com.chaeshin.boo.service.restaurant.RestaurantService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RestaurantAPITest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtAuthService jwtAuthService;

    @Autowired
    RestaurantService restaurantService;

    final String AUTH = "Bearer token";
    final String ACCESSTOKEN = "token";

    @Test
    @DisplayName("GET : restaurant/detail/integ/{restaurant_id}/")
    void 식당_상세정보_조회() throws Exception {
        // given
        Long restaurantId = 1L;

        // when
        when(jwtAuthService.parseToken(AUTH)).thenReturn(ACCESSTOKEN);
        when(jwtAuthService.verifyAccessToken(ACCESSTOKEN)).thenReturn(true);

        // then
        MvcResult result = mockMvc.perform(get("/restaurant/detail/integ/" + restaurantId + "/")
                        .header("Authorization", AUTH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("GET : restaurant/menu/{restuarnat_id}/")
    void 식당_메뉴_조회() throws Exception {
        // given
        Long restaurantId = 1L;

        // then
        mockMvc.perform(get("/restaurant/menu/" + restaurantId + "/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
