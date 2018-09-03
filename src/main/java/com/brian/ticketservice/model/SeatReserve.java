package com.brian.ticketservice.model;

import java.util.List;

/**
 * Created by brianzhang on 8/30/18.
 */
public class SeatReserve {

    private String confirmationCode;
    private String customerEmail;
    private List<Seat> seats;

    public SeatReserve(String confirmationCode,
                       String customerEmail,
                       List<Seat> seats) {
        this.confirmationCode = confirmationCode;
        this.seats = seats;
        this.customerEmail = customerEmail;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
