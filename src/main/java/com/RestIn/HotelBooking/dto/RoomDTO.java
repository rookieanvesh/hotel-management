package com.RestIn.HotelBooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    private List<BookingDTO> bookings;
}
/* This DTO is designed to transfer room data between different parts of the application or to the client,
 potentially hiding certain implementation details of the underlying entity.
 The use of BigDecimal for roomPrice suggests precise handling of monetary values,
 and the List<BookingDTO> indicates that a room can have multiple associated bookings.*/
