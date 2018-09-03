package com.brian.ticketservice.model;

import java.time.Instant;
import java.util.List;

/**
 * Created by brianzhang on 8/30/18.
 */
public class SeatHold {

    private int seatHoldId;
    private String customerEmail;
    private List<Seat> seats;
    private Instant holdTime;

    public SeatHold(int seatHoldId,
                    String customerEmail,
                    List<Seat> seats,
                    Instant expireTime) {
        this.seatHoldId = seatHoldId;
        this.customerEmail = customerEmail;
        this.seats = seats;
        this.holdTime = expireTime;
    }

    public boolean isExpire() {
        return !holdTime.isAfter(Instant.now());
    }

    public int getSeatHoldId() {
        return seatHoldId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Instant getHoldTime() {
        return holdTime;
    }
}
