package com.RestIn.HotelBooking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Enter email")
    private String email;
    @NotBlank(message = "Enter password")
    private String password;

}
