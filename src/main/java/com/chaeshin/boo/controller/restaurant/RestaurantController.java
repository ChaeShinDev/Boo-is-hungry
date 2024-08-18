package com.chaeshin.boo.controller.restaurant;

import com.chaeshin.boo.controller.dto.BaseResponseDto;
import com.chaeshin.boo.controller.dto.DataResponseDto;
import com.chaeshin.boo.controller.dto.restaurant.RestaurantDetailInfoDto;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.exception.auth.TokenException;
import com.chaeshin.boo.service.member.auth.JwtAuthService;
import com.chaeshin.boo.service.restaurant.RestaurantService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final JwtAuthService jwtAuthService;


    /**
     * 식당 상세정보 조회
     * @param auth
     * @param restaurantId
     * @return
     */
    @GetMapping("restaurant/detail/integ/{restaurant_id}/")
    public ResponseEntity<BaseResponseDto> getRestaurantDetailInfo(@RequestHeader("Authorization") String auth,
                                                                   @PathVariable("restaurant_id") Long restaurantId) {

        String accessToken = jwtAuthService.parseToken(auth);

        try {
            if(jwtAuthService.verifyAccessToken(accessToken)){
                Restaurant restaurant = restaurantService.getRestaurantByRestaurantId(restaurantId);
                RestaurantDetailInfoDto dto = RestaurantDetailInfoDto.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .phone(restaurant.getPhone())
                        .address(restaurant.getAddress())
                        .longitude(restaurant.getLongitude())
                        .latitude(restaurant.getLatitude())
                        .category(restaurant.getCategory())
                        .businessHours(restaurant.getBusinessHours())
                        .reviewCnt(restaurant.getReviewCnt())
                        .scoreAvg(restaurant.getScoreAvg())
                        .menus(restaurant.getMenus())
                        .reviews(restaurant.getReviews())
                        .build();

                return ResponseEntity.ok().body(new DataResponseDto<>("식당 세부정보 불러오기 성공", dto));
            }
            else {
                throw new TokenException("토큰이 유효하지 않습니다.");
            }
        }
        catch (TokenException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponseDto("토큰이 유효하지 않습니다."));
        }

        catch (NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDto("해당 식당이 존재하지 않습니다."));
        }
    }

    @GetMapping("restaurant/menu/{restaurant_id}/")
    public ResponseEntity<BaseResponseDto> getMenus(@PathVariable("restaurant_id") Long restaurantId){
        try {
            Restaurant restaurant = restaurantService.getRestaurantByRestaurantId(restaurantId);
            return ResponseEntity.ok().body(new DataResponseDto<>("식당 메뉴 불러오기 성공", restaurant.getMenus()));
        }
        catch (NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDto("해당 식당이 존재하지 않습니다."));
        }
    }
}
