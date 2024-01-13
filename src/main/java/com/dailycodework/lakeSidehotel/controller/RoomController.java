package com.dailycodework.lakeSidehotel.controller;

import com.dailycodework.lakeSidehotel.exception.PhotoRetrievalException;
import com.dailycodework.lakeSidehotel.model.BookedRoom;
import com.dailycodework.lakeSidehotel.model.Room;
import com.dailycodework.lakeSidehotel.response.BookingResponse;
import com.dailycodework.lakeSidehotel.response.RoomResponse;
import com.dailycodework.lakeSidehotel.service.BookingService;
import com.dailycodework.lakeSidehotel.service.IBookedRoomService;
import com.dailycodework.lakeSidehotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@CrossOrigin(origins = "*")
public class RoomController {
    @Autowired
    private final IRoomService roomService;
    private  final BookingService bookingService;
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

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms=roomService.getAllRooms();
        List<RoomResponse> roomResponses=new ArrayList<> ();
        for(Room room:rooms){
            byte[] photoBytes=roomService.getRoomPhotoByRoomId(room.getId ());
            if(photoBytes != null && photoBytes.length>0){
                String base64Photo= Base64.encodeBase64String (photoBytes);
                RoomResponse roomResponse=getRoomResponse(room);
                roomResponse.setPhoto (base64Photo);
                roomResponses.add (roomResponse);

            }
        }
        return ResponseEntity.ok (roomResponses);
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings=getAllBookingsByRoomId(room.getId ());
        List<BookingResponse> bookingInfo=bookings.
                stream ().
                map (booking->new BookingResponse (booking.getBookingId (),booking.getCheckInDate (),
                        booking.getCheckOutDate (),
                        booking.getBookingConfirmationCode ())).toList ();
        byte[] photoBytes=null;
        Blob photoBlob=room.getPhoto ();
        if(photoBlob !=null ){
            try{
                photoBytes = photoBlob.getBytes (1,(int) photoBlob.length ());

            }catch(SQLException e){
                throw new PhotoRetrievalException ("Error in retrieving photo");

            }

        }
        return new RoomResponse (room.getId (),room.getRoomType (),room.getRoomPrice (),
                room.isBooked (),photoBytes,bookingInfo);

    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomID(roomId);

    }
}
