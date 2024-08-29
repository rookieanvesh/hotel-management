package com.RestIn.HotelBooking.service.impl;

import com.RestIn.HotelBooking.dto.BookingDTO;
import com.RestIn.HotelBooking.dto.Response;
import com.RestIn.HotelBooking.entity.Booking;
import com.RestIn.HotelBooking.entity.Room;
import com.RestIn.HotelBooking.entity.User;
import com.RestIn.HotelBooking.exception.OurException;
import com.RestIn.HotelBooking.repo.BookingRepository;
import com.RestIn.HotelBooking.repo.RoomRepository;
import com.RestIn.HotelBooking.repo.UserRepository;
import com.RestIn.HotelBooking.service.interfac.IBookingService;
import com.RestIn.HotelBooking.service.interfac.IRoomService;
import com.RestIn.HotelBooking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();
        try{
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new IllegalArgumentException("Check-in date must be before check-out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room not found"));
            User user = userRepository.findById(userId).orElseThrow(()-> new OurException("User not found"));
            List<Booking> existingBookings = room.getBookings();
            if(!roomIsAvailable(bookingRequest, existingBookings)){
                throw new OurException("Room not available for the selected date range");
            }
            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

            response.setMessage("Successful");
            response.setStatusCode(200);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving booking" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try{
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()->new OurException("Booking not found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setBooking(bookingDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting booking by confirmation code" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try{
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setBookingList(bookingDTOList);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try{
            bookingRepository.findById(bookingId).orElseThrow(()->new OurException("Booking not found"));
            bookingRepository.deleteById(bookingId);
            response.setMessage("Successful");
            response.setStatusCode(200);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
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
