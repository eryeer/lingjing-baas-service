package com.onchain.entities;

import lombok.Data;


@Data
public class TokenResponse {
    String accessToken;
    String refreshToken;
    Integer expiresIn;
}
