package com.chaeshin.boo.controller;

import com.chaeshin.boo.exception.ExpiredTokenException;
import com.chaeshin.boo.exception.TokenNotFoundException;
import com.chaeshin.boo.service.restaurant.RestaurantService;
import com.chaeshin.boo.service.restaurant.dto.MapInfoDto;
import com.chaeshin.boo.service.restaurant.dto.MenuDto;
import com.chaeshin.boo.service.restaurant.dto.RestaurantInfoDto;
import com.chaeshin.boo.service.restaurant.dto.RestaurantSearchDto;
import com.chaeshin.boo.utils.geocoding.GeoCoding;
import com.chaeshin.boo.utils.jwt.JwtProvider;
import com.chaeshin.boo.utils.ResponseDto;
import com.chaeshin.boo.utils.geocoding.CoordinateDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final JwtProvider jwtProvider;
    private final GeoCoding geoCoding;

    @GetMapping("/location/")
    public ResponseEntity<ResponseDto<LinkedList<MapInfoDto>>> getLocation(
            HttpServletRequest request) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        try {
            return new ResponseEntity<>(
                    restaurantService.getAllRestaurants(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/detail/integ/{restaurantId}/")
    public ResponseEntity<ResponseDto<RestaurantInfoDto>> getRestaurantDetail(
            HttpServletRequest request, @PathVariable Long restaurantId) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        try {
            return new ResponseEntity<>(
                    restaurantService.getRestaurantDetail(restaurantId),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/menu/{restaurantId}/")
    public ResponseEntity<List<MenuDto>> getMenu(HttpServletRequest request,
                                                 @PathVariable Long restaurantId) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        try {
            return new ResponseEntity<>(
                    restaurantService.getMenuList(restaurantId),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/{restaurantName}/")
    public ResponseEntity<ResponseDto<List<RestaurantSearchDto>>> getRestaurantSearch(
            HttpServletRequest request, @PathVariable String restaurantName) {
        try {jwtProvider.validateToken(request);}
        catch (ExpiredTokenException e) {throw e;}
        catch (TokenNotFoundException e) {throw e;}

        try {
            return new ResponseEntity<>(
                    restaurantService.searchRestaurantByName(restaurantName),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/address/")
    public ResponseEntity<ResponseDto<CoordinateDto>> getCoordinate(@RequestBody Map<String, String> request) {
        ResponseDto<CoordinateDto> result = restaurantService.getRestaurantCoordinates(request);

        try {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
