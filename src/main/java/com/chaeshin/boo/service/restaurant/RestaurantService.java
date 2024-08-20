package com.chaeshin.boo.service.restaurant;

import com.chaeshin.boo.service.restaurant.dto.MapInfoDto;
import com.chaeshin.boo.service.restaurant.dto.MenuDto;
import com.chaeshin.boo.service.restaurant.dto.RestaurantInfoDto;
import com.chaeshin.boo.utils.ResponseDto;
import com.chaeshin.boo.utils.geocoding.CoordinateDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface RestaurantService {

    /*모든 식당의 위경도 조회*/
    ResponseDto<LinkedList<MapInfoDto>> getAllRestaurants();

    /*식당 상세 정보 조회*/
    ResponseDto<RestaurantInfoDto> getRestaurantDetail(Long restaurantId);

    /*식당의 메뉴 목록 조회*/
    List<MenuDto> getMenuList(Long restaurantId);

    /*식당 이름으로 검색*/
    ResponseDto searchRestaurantByName(String restaurantName);

    /*식당의 주소로 좌표 조회*/
    ResponseDto<CoordinateDto> getRestaurantCoordinates(Map<String, String> request);
}
