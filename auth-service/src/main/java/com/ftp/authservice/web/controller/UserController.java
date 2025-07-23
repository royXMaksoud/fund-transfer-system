package com.ftp.authservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * UserController class is responsible for handling requests related to the current authenticated user.
 * It provides an endpoint to fetch the user profile information.
 */
@RestController
@RequestMapping("/users") // Maps the URL to the "/users" endpoint
@Tag(name = "User Profile", description = "Operations related to current authenticated user")
@SecurityRequirement(name = "bearerAuth") // Ensures that all endpoints in this controller require authentication via JWT
public class UserController {

    /**
     * Endpoint to get the current authenticated user's profile.
     *
     * @param authentication Contains the authentication details of the logged-in user
     * @return ResponseEntity containing the user's profile data
     */
    @Operation(
            summary = "Get current user profile",
            description = "Returns information about the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    })
    @GetMapping("/me") // Maps to the GET request for the "/me" endpoint
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        // Check if the authentication is valid (i.e., the user is authenticated)
        if (authentication == null || !authentication.isAuthenticated()) {
            // If the user is not authenticated, return an unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        // Retrieve the username of the authenticated user from the authentication object
        String username = authentication.getName();

        // Return the user's profile with a success message
        return ResponseEntity.ok(Map.of(
                "username", username, // Return the username of the authenticated user
                "message", "Token is valid and this is your profile" // Confirmation message indicating the token is valid
        ));
    }
}
