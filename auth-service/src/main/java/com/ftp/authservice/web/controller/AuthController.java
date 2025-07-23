package com.ftp.authservice.web.controller;

import com.ftp.authservice.domain.model.User;
import com.ftp.authservice.domain.ports.in.LoginUseCase;
import com.ftp.authservice.domain.ports.in.RegisterUserUseCase;
import com.ftp.authservice.exception.InvalidTokenException;
import com.ftp.authservice.infrastructure.db.entities.RefreshTokenEntity;
import com.ftp.authservice.infrastructure.security.JwtTokenProvider;
import com.ftp.authservice.infrastructure.security.RefreshTokenService;
import com.ftp.authservice.web.dto.JwtResponseDTO;
import com.ftp.authservice.web.dto.LoginRequestDTO;
import com.ftp.authservice.web.dto.RegisterRequestDTO;
import com.ftp.authservice.web.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AuthController handles all authentication related operations.
 * It manages user login, registration, profile retrieval, and token refresh functionality.
 */
@RestController
@RequestMapping("/auth") // All endpoints in this controller will have the "/auth" base URL
@Tag(name = "Authentication", description = "Endpoints for user login and registration") // Swagger tag for API documentation
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * Constructor to inject dependencies for authentication services.
     */
    public AuthController(LoginUseCase loginUseCase,
                          RegisterUserUseCase registerUserUseCase,
                          RefreshTokenService refreshTokenService,
                          JwtTokenProvider jwtTokenProvider) {
        this.loginUseCase = loginUseCase;
        this.registerUserUseCase = registerUserUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Login API to authenticate users using username and password.
     * Generates JWT and Refresh token upon successful authentication.
     *
     * @param request contains the login credentials (username and password)
     * @return ResponseEntity containing JWT and Refresh tokens
     */
    @Operation(summary = "Login with username and password", description = "Returns JWT token if credentials are valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        // Authenticate user and validate login credentials
        User user = loginUseCase.login(request.getUsername(), request.getPassword());

        // Generate JWT token using the user's credentials
        String accessToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());

        // Generate refresh token for the user
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user.getUsername(), user.getRole());

        // Return the generated JWT and refresh tokens
        return ResponseEntity.ok(new JwtResponseDTO(accessToken, refreshToken.getToken(), user.getUsername()));
    }

    /**
     * Register a new user by providing the necessary details.
     * It takes user details, validates them, and stores them in the database.
     *
     * @param request contains user registration details (username, password, etc.)
     * @return ResponseEntity containing the registered user's information
     */
    @Operation(summary = "Register new user", description = "Registers a new user with username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        // Convert the registration DTO to a domain model and register the user
        User created = registerUserUseCase.register(request.toDomain());

        // Return the registered user information
        return ResponseEntity.ok(UserResponseDTO.from(created));
    }

    /**
     * Get the current user profile using a valid JWT token.
     * The token is extracted from the Authorization header.
     *
     * @param request to retrieve the Authorization header containing the JWT token
     * @return ResponseEntity containing the user's profile information
     */
    @Operation(summary = "Get user profile", description = "Returns user info if JWT token is valid", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token valid"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing token")
    })
    @GetMapping("/user/profile")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Check if the token is missing or invalid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Missing or invalid Authorization header");
        }

        // Extract the JWT token and retrieve the username
        String token = authHeader.substring(7); // Extract token from the "Bearer " prefix
        String username = jwtTokenProvider.getUsernameFromToken(token);

        // Return the user's profile data
        return ResponseEntity.ok(Map.of("username", username, "message", "Token is valid and this is your profile"));
    }

    /**
     * Refresh the JWT token using the provided refresh token.
     * It returns a new access token if the refresh token is valid.
     *
     * @param request contains the refresh token
     * @return ResponseEntity with the new access token
     */
    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String requestToken = request.get("refreshToken");

        // Check if the refresh token exists and is not expired
        return refreshTokenService.findByToken(requestToken)
                .map(token -> {
                    if (refreshTokenService.isExpired(token)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
                    }
                    // Generate a new access token if the refresh token is valid
                    String accessToken = jwtTokenProvider.generateToken(token.getUsername(), token.getRole());
                    return ResponseEntity.ok(Map.of("accessToken", accessToken));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token"));
    }
}
