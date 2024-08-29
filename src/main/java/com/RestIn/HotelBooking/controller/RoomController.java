package com.RestIn.HotelBooking.controller;

import com.RestIn.HotelBooking.dto.Response;
import com.RestIn.HotelBooking.service.interfac.IBookingService;
import com.RestIn.HotelBooking.service.interfac.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private IRoomService roomService;

    @Autowired
    private IBookingService bookingService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")// we only want our admin to add a room
    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo", required = false)MultipartFile photo,
            @RequestParam(value = "roomType", required = false)String roomType,
            @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false)String roomDescription
            ){
        if(photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null
        || roomDescription == null){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all the fields(photo,roomType, roomPrice, roomDescription");
        }
        Response response = roomService.addNewRoom(photo, roomType, roomPrice, roomDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms(){
        Response response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
