package com.brian.ticketservice.common;

import com.brian.ticketservice.model.Seat;
import com.brian.ticketservice.model.SeatReserve;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by brianzhang on 8/31/18.
 */
public final class Utils {

    private static volatile AtomicInteger id = new AtomicInteger();
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static int generateSeatId() {
        //increment by 1
        return id.addAndGet(1);
    }

    public static synchronized boolean isValidEmail(String customerEmail) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(customerEmail);
        return matcher.matches();
    }

    public static String generateConfirmationCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static synchronized void displaySeats(List<Seat> seats) {
        for (int i = 0; i < seats.size(); i++) {
            System.out.println("Seat Id: "+ seats.get(i).getId() +" (Row: " + seats.get(i).getRow() + ", Number:" + seats.get(i).getNumber() + ")");
        }
    }

    //private constructor
    private Utils() {
    }
}
