package com.RestIn.HotelBooking.service.impl;

import com.RestIn.HotelBooking.dto.BookingDTO;
import com.RestIn.HotelBooking.dto.Response;
import com.RestIn.HotelBooking.entity.Booking;
import com.RestIn.HotelBooking.entity.Room;
import com.RestIn.HotelBooking.entity.User;
import com.RestIn.HotelBooking.exception.GlobalExceptionHandler;
import com.RestIn.HotelBooking.repo.BookingRepository;
import com.RestIn.HotelBooking.repo.RoomRepository;
import com.RestIn.HotelBooking.repo.UserRepository;
import com.RestIn.HotelBooking.service.interfac.IBookingService;
import com.RestIn.HotelBooking.service.interfac.IRoomService;
import com.RestIn.HotelBooking.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService implements IBookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

        @Override
        @Transactional
        public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
            Response response = new Response();
            try {
                validateBookingDates(bookingRequest);

                Room room = roomRepository.findById(roomId)
                        .orElseThrow(() -> new GlobalExceptionHandler("Room Not Found"));
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new GlobalExceptionHandler("User Not Found"));

                synchronized (this.getClass()) {
                    room = roomRepository.findByIdWithPessimisticLock(roomId)
                            .orElseThrow(() -> new GlobalExceptionHandler("Room Not Found"));

                    if (!roomIsAvailable(bookingRequest, room.getBookings())) {
                        throw new GlobalExceptionHandler("Room not Available for selected date range");
                    }

                    bookingRequest.setRoom(room);
                    bookingRequest.setUser(user);
                    String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
                    bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);

                    Booking savedBooking = bookingRepository.save(bookingRequest);
                    room.getBookings().add(savedBooking);
                    roomRepository.save(room);

                    response.setStatusCode(200);
                    response.setMessage("successful");
                    response.setBookingConfirmationCode(bookingConfirmationCode);
                }
            } catch (ObjectOptimisticLockingFailureException e) {
                response.setStatusCode(409);
                response.setMessage("Booking failed due to concurrent modification. Please try again.");
            } catch (GlobalExceptionHandler e) {
                response.setStatusCode(404);
                response.setMessage(e.getMessage());
            } catch (Exception e) {
                response.setStatusCode(500);
                response.setMessage("Error saving booking: " + e.getMessage());
            }
            return response;
        }

        private void validateBookingDates(Booking booking) {
            if (booking.getCheckOutDate() == null ||
                    !booking.getCheckOutDate().isAfter(LocalDate.now())) {
                throw new GlobalExceptionHandler("The check-out date must be set for a future date");
            }
            if (booking.getCheckInDate() == null ||
                    booking.getCheckInDate().isBefore(LocalDate.now())) {
                throw new GlobalExceptionHandler("The check-in date must be today or a future date");
            }
            if (!booking.getCheckOutDate().isAfter(booking.getCheckInDate())) {
                throw new GlobalExceptionHandler("Check-out date must be after check-in date");
            }
        }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new GlobalExceptionHandler("Booking not found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setBooking(bookingDTO);

        } catch (GlobalExceptionHandler e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting booking by confirmation code" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setBookingList(bookingDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try {
            bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new GlobalExceptionHandler("Booking not found"));
            bookingRepository.deleteById(bookingId);
            response.setMessage("Successful");
            response.setStatusCode(200);

        } catch (GlobalExceptionHandler e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while cancellation of booking" + e.getMessage());
        }
        return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings){
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
