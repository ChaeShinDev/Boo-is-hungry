package com.chaeshin.boo.service.restaurant;

import com.chaeshin.boo.service.restaurant.dto.geo.KakaoGeoResponseDto;
import com.chaeshin.boo.service.restaurant.dto.geo.ResponseDocumentDto;
import com.chaeshin.boo.service.restaurant.dto.geo.ResponseMetaDto;
import com.chaeshin.boo.domain.restaurant.Category;
import com.chaeshin.boo.domain.restaurant.Restaurant;
import com.chaeshin.boo.repository.restaurant.RestaurantRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * 카카오에서 제공하는 지도 API를 활용하여 식당의 위치를 비롯한 다양한 정보를 가공하고 Web Layer로 전달하거나 RestaurantService로 전달하는 Service.
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoGeoService {

    private final RestaurantRepository restaurantRepository;

    @Value("${kakao.appKey}")
    private String apiKey;

    private final String tokenType = "KakaoAK";
    private final String baseUrl = "https://dapi.kakao.com/v2/local/search/category.json?";
    private final String restaurantCode = "FD6"; // 음식점 카테고리 코드.
    private final String cafeCode = "CE7"; // 카페 카테고리 코드.

    private final String hufsSeoulX = "127.05787102153596"; // 한국외국어대학교 서울캠퍼스 본관 경도.
    private final String hufsSeoulY = "37.59727904279873"; // 한외국어대학교 서울캠퍼스 본관 위도.


    /**
     * 카카오 지도 API 로 '한국외국어대학교 반경 788미터 이내의 모든 음식점과 카페' 를 요청한 후 Restaurant 객체로 Bind 후 Return.
     * <br></br>
     *
     * @return 한국외대 반경 788미터 이내 식당/카페 전체 목록.
     */
    @Transactional
    public void initAllRestaurant() {

        List<Restaurant> restaurantList = new ArrayList<>(); // 저장할 레스토랑 목록.
        boolean isEndPage = false; // 현재 살펴보고 있는 응답이 마지막 페이지인지 여부.
        int pageNum = 1; // 요청 페이지 및 현재 조회 중인 페이지. Kakao 지도 API 스펙 상 45페이지까지 조회 가능.
        while(!isEndPage){
            int currentPageNum = pageNum; // Lambda 내부에 사용하는 변수는 final 이거나 이에 준하는 성격을 가진 변수여야함.(WHY?)
            KakaoGeoResponseDto currentResponse = WebClient.create(baseUrl)
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("category_group_code", restaurantCode)
                            .queryParam("x", hufsSeoulX)
                            .queryParam("y", hufsSeoulY)
                            .queryParam("radius", 788)
                            .queryParam("page", currentPageNum)
                            .build())
                    .header("Authorization", tokenType + " " + apiKey)
                    .retrieve()
                    .bodyToMono(KakaoGeoResponseDto.class)
                    .block();

            ResponseMetaDto currentMeta = currentResponse.getMeta();
            List<ResponseDocumentDto> currentDocuments = currentResponse.getDocuments();

            for(ResponseDocumentDto document : currentDocuments){
                String parsedCategory = document.getCategoryGroupName();
                Restaurant restaurant = Restaurant.builder()
                        .address(document.getAddressName())
                        .name(document.getName())
                        .phone(document.getPhone())
                        .longitude(document.getLongitude())
                        .latitude(document.getLatitude())
                        .scoreAccum(0) // 최초 0
                        .scoreAvg(new BigDecimal(0)) // 최초 0
                        .category(Category.KOREAN) // 임의. Category 세부 분류 목록을 파악하면 가능한데... 정리되어 공개된 자료가 없음.
                        .businessHours(null) // 해당 정보 조회 불가.
                        .imageUrl("") // 해당 정보 조회 불가.
                        .build();

                restaurantList.add(restaurant);
            }
            isEndPage = currentMeta.isEnd();
            pageNum++;
        }

        restaurantRepository.saveAll(restaurantList);

    }



    private Category parseCategoryFromString(String categoryName){
        Category category = null;

        switch (categoryName) {
            case "분식" :
                category = Category.SCHOOLFOOD;
                break;

            case "술집":
                category =  Category.ALCOHOL;
                break;
            case "아시아음식" :
                category =  Category.WORLDFOOD;
                break;

            case "양식" :
                category = Category.WESTERN;
                break;

            case "일식" :
                category = Category.JAPANESE;
                break;

            case "중식" :
                category = Category.CHINESE;
                break;

            case "패스트푸드" :
                category = Category.FASTFOOD;
                break;

            case "패밀리레스토랑" :
                category = Category.WESTERN;
                break;

            case "피자" :
                category = Category.WESTERN;
                break;

            case "치킨" :
                category = Category.WESTERN;
                break;

            case "한식" :
                category = Category.KOREAN;
                break;

            case "뷔페" :
                category = Category.WESTERN;
                break;
        }
        return category;
    }
}
