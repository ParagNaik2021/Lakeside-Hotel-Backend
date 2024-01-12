package com.dailycodework.lakeSidehotel.model;
import org.apache.commons.lang3.RandomStringUtils;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false;
    @Lob
    private Blob photo;

    @OneToMany(mappedBy="room",fetch = FetchType.LAZY,cascade = CascadeType.ALL) //one person can book many rooms so one to many
    private List<BookedRoom> bookings;

    public Room() {
        this.bookings = new ArrayList<>();
    }
    public void addBooking(BookedRoom booking){
        if(bookings==null){
            bookings=new ArrayList<> ();
        }
        bookings.add (booking);
        booking.setRoom (this);
        isBooked=true;
        String bookingCode= RandomStringUtils.randomNumeric (10); //need to add dependency
        booking.setBookingConfirmationCode (bookingCode);
    }
}
