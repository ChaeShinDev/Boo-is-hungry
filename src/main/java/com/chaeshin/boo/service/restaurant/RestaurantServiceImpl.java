package com.chaeshin.boo.service.restaurant;

import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import com.chaeshin.boo.repository.restaurant.menu.MenuRepository;
import com.chaeshin.boo.repository.review.ReviewRepository;
import com.chaeshin.boo.service.restaurant.dto.MapInfoDto;
import com.chaeshin.boo.service.restaurant.dto.MenuDto;
import com.chaeshin.boo.service.restaurant.dto.RestaurantInfoDto;
import com.chaeshin.boo.service.restaurant.dto.RestaurantSearchDto;
import com.chaeshin.boo.utils.ResponseDto;
import com.chaeshin.boo.utils.geocoding.GeoCoding;
import com.chaeshin.boo.utils.geocoding.CoordinateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final GeoCoding geoCoding;


    @Override
    public ResponseDto<LinkedList<MapInfoDto>> getAllRestaurants() {
        List<MapInfoDto> restaurants = restaurantRepository.findAll()
                .stream().map(o -> new MapInfoDto(o))
                .collect(Collectors.toList());

        return new ResponseDto("식당 위/경도 반환 성공", restaurants);
    }

    @Override
    public ResponseDto<RestaurantInfoDto> getRestaurantDetail(Long restaurantId) {

        return new ResponseDto<>("식당 세부정보 불러오기 성공", new RestaurantInfoDto()
                        .updateRestaurant(restaurantRepository.findByIdWithMenus(restaurantId))
                        .updateReviews(reviewRepository.findAllByRestaurantIdWithImage(restaurantId)));
    }

    @Override
    public List<MenuDto> getMenuList(Long restaurantId) {
        return menuRepository.findAllByRestaurantId(restaurantId).stream()
                .map(o -> new MenuDto(o)).collect(Collectors.toList());
    }

    @Override
    public ResponseDto<List<RestaurantSearchDto>> searchRestaurantByName(String restaurantName) {
        List<RestaurantSearchDto> result = restaurantRepository.findAllByNameContaining(restaurantName)
                .stream().map(o -> new RestaurantSearchDto(o)).collect(Collectors.toList());
        return new ResponseDto<>("식당 이름으로 검색 성공", result);
    }

    @Override
    public ResponseDto<CoordinateDto> getRestaurantCoordinates(Map<String, String> request) {
        String address = request.get("address");
        return new ResponseDto<>("좌표 조회 성공", geoCoding.geoCode(address));
    }
}
