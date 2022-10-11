package com.staya.asap.Configuration.Security.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String key;

}