package com.smartbooking.app.data.model;

public class LogoutRequest {
    private final String refresh;

    public LogoutRequest(String refresh) {
        this.refresh = refresh;
    }

    public String getRefresh() { return refresh; }
}