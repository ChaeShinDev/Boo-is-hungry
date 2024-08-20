package com.chaeshin.boo.service.member.auth.dto;

import lombok.Data;

@Data
public class GoogleMemberInfoDto {

    private String id;
    private String email;
    private String verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
}
