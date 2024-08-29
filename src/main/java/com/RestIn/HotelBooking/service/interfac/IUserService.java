package com.RestIn.HotelBooking.service.interfac;

import com.RestIn.HotelBooking.dto.LoginRequest;
import com.RestIn.HotelBooking.dto.Response;
import com.RestIn.HotelBooking.entity.User;

public interface IUserService {
    Response register(User user);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserBookingHistory(String userId);
    Response deleteUser(String userId);
    Response getUserById(String userId);
    Response getMyInfo(String email);

}
