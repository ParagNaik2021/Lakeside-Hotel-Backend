package com.dailycodework.lakeSidehotel.service;

import com.dailycodework.lakeSidehotel.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface IBookedRoomService {
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice);
    // Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice);
}
