package com.chaeshin.boo.service.member.auth;

public enum Redirection {
    BACK("http://localhost:8080/accounts/login/"),
    DOMAIN("https://hufsmeals.shop/accounts/google/"),
    FRONT("http://localhost:5173/loginLoading");

    private String redirectUri;

    Redirection(final String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
