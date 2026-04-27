package com.zosh.service;

import com.zosh.payload.dto.SignupDTO;
import com.zosh.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String username, String password) throws Exception;
    AuthResponse signup(SignupDTO req) throws Exception;
    AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception;
}