package com.ftp.authservice.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Response containing JWT token and username")
public class JwtResponseDTO {

    @Schema(description = "JWT token to be used in Authorization header", example = "eyJhbGci...")
    private String token;

    private String refreshToken;


    @Schema(description = "Username of the authenticated user", example = "admin")
    private String username;


    public JwtResponseDTO(String token, String refreshToken, String username) {
            this.token = token;
            this.refreshToken = refreshToken;
            this.username = username;
        }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
    public String getRefreshToken() {
        return refreshToken;
    }

}
