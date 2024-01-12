package com.dailycodework.lakeSidehotel.controller;

import com.dailycodework.lakeSidehotel.model.Room;
import com.dailycodework.lakeSidehotel.response.RoomResponse;
import com.dailycodework.lakeSidehotel.service.IBookedRoomService;
import com.dailycodework.lakeSidehotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@CrossOrigin(origins = "*")
public class RoomController {
    @Autowired
    private final IRoomService roomService;
    @PostMapping("/add/new-room")

    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam ("photo") MultipartFile photo,
                                                   @RequestParam ("roomType") String roomType,
                                                   @RequestParam ("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom (photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse (savedRoom.getId (), savedRoom.getRoomType (), savedRoom.getRoomPrice ());
        return ResponseEntity.ok (response);
    }

    @GetMapping("/room/types")
    public List<String> getroomTypes(){
        System.out.println (roomService.getAllRoomTypes());
        return roomService.getAllRoomTypes();
    }
}
