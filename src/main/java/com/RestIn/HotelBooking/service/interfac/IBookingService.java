package com.RestIn.HotelBooking.service.interfac;

import com.RestIn.HotelBooking.dto.Response;
import com.RestIn.HotelBooking.entity.Booking;

public interface IBookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);

}
