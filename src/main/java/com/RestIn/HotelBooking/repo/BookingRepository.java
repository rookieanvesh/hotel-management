package com.RestIn.HotelBooking.repo;

import com.RestIn.HotelBooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Optional<Booking> findBookingConfirmationCode(String confirmationCode);
}
