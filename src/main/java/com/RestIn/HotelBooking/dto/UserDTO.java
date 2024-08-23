package com.RestIn.HotelBooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*It specifies that only non-null properties should be included in the serialized JSON output.
This is particularly useful when you want to avoid sending null values in your JSON responses,
making your API cleaner and more efficient. */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private List<BookingDTO> bookings = new ArrayList<>();
}
