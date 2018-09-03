package com.brian.ticketservice.serviceImpl;


import com.brian.ticketservice.common.Utils;
import com.brian.ticketservice.model.Seat;
import com.brian.ticketservice.model.SeatHold;
import com.brian.ticketservice.model.SeatReserve;
import com.brian.ticketservice.service.TicketService;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Created by brianzhang on 8/30/18.
 */
public class TicketServiceImpl implements TicketService, Runnable {

    private final int capacity;
    private volatile AtomicInteger reservedCount = new AtomicInteger();
    private volatile AtomicInteger holdCount = new AtomicInteger();
    private final int holdSeconds;
    //key: confirmation code
    private Map<String, SeatReserve> seatReserveMap = new ConcurrentHashMap<>();
    //key: seatHold Id
    private Map<Integer, SeatHold> seatHoldMap = new ConcurrentHashMap<>();
    //key: seat id
    private Map<Integer, Seat> seatMap = new ConcurrentHashMap<>();
    //the index of boolean array Link to the seat id in seatMap
    private final boolean[] venueSeats;

    public TicketServiceImpl(int row, int column, int holdSeconds) {
        this.capacity = row * column;
        this.holdSeconds = holdSeconds;
        this.venueSeats = new boolean[this.capacity];
        initiateSeats(row, column);
    }

    /**
     * The number of seats in the venue that are neither held nor reserved
     */
    public int numSeatsAvailable() {
        refreshHoldSeats();
        return capacity - (this.reservedCount.get() + this.holdCount.get());
    }

    /**
     * Find and hold the best available seats for a customer
     * Here, we consider the most front seats is best, :)
     *
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return seatHold
     */
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        SeatHold seatHold = null;
        if (numSeats < 1) {
            System.out.println("The input numbers of seat is invalid.");
            return seatHold;
        }
        if (customerEmail == null || !Utils.isValidEmail(customerEmail)) {
            System.out.println("Please provide the valid Email.");
            return seatHold;
        }
        //release the hold seats if it's expire
        refreshHoldSeats();
        //check whether has enough seats
        if (numSeats > numSeatsAvailable()) {
            System.out.println("Sorry, we don't have enough available seats." +
                    "/n The current available seats is " + numSeatsAvailable());
            return seatHold;
        }
        int seatHoldId = Utils.generateSeatId();
        List<Seat> holdSeats = new ArrayList<>();
        seatHold = new SeatHold(seatHoldId, customerEmail, holdSeats, Instant.now().plusSeconds(holdSeconds));
        //Find the beat seat for customer
        for (int i = 1; i <= venueSeats.length; i++) {
            if (!venueSeats[i]) {
                numSeats--;
                holdSeats.add(seatMap.get(i));
                venueSeats[i] = true;
            }
            if (numSeats == 0) {
                break;
            }
        }
        holdCount.addAndGet(numSeats);
        seatHoldMap.put(seatHoldId, seatHold);
        return seatHold;
    }

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the
     *                      seat hold is assigned
     * @return confirmation code
     */
    public synchronized String reserveSeats(int seatHoldId, String customerEmail) {
        String confirmationCode = null;
        if (customerEmail == null || !Utils.isValidEmail(customerEmail)) {
            System.out.println("The input email address is invalid");
            return confirmationCode;
        }

        SeatHold seatHold = seatHoldMap.get(seatHoldId);
        if (seatHold == null) {
            System.out.println("Not able to find your held seats information");
            return confirmationCode;
        }

        if (seatHold.isExpire()) {
            System.out.println("Your hold seat has expired, please re-select your seat.");
            return confirmationCode;
        }

        if (!seatHold.getCustomerEmail().equals(customerEmail)) {
            System.out.println("Customer Email information doesn't match, not able to process the request.");
            return confirmationCode;
        }

        confirmationCode = Utils.generateConfirmationCode();
        SeatReserve seatReserve = new SeatReserve(confirmationCode, seatHold.getCustomerEmail(), seatHold.getSeats());
        seatReserveMap.put(confirmationCode, seatReserve);
        reservedCount.addAndGet(seatHold.getSeats().size());

        //clean seatHold from seatHoldMap after commit to reserve
        seatHoldMap.remove(seatHoldId);
        return confirmationCode;
    }

    /**
     * Refresh the hold seats to clean the expire hold
     */
    private synchronized void refreshHoldSeats() {
        seatHoldMap.entrySet().stream().forEach(element -> {
            SeatHold temp = (SeatHold) element.getValue();
            if (temp.isExpire()) {
                temp.getSeats().stream().forEach(seatNum -> {
                    venueSeats[seatMap.get(seatNum).getId()] = false;
                    holdCount.decrementAndGet();
                });
            }
            seatHoldMap.remove(temp.getSeatHoldId());
        });
    }

    /**
     * Initiate the seat with row and column, and assign the id for each seat
     *
     * @param row
     * @param column
     */
    public void initiateSeats(int row, int column) {
        if (row == 0 || column == 0) {
            System.out.println("The row or column number is invalid.");
            return;
        }
        int id = 1;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= column; j++) {
                seatMap.put(id, new Seat(i, j, id));
                id++;
            }
        }
    }

    /**
     * Start from here is the process of running this ticket service, the above is the
     */
    @Override
    public void run() {
        try {
            Scanner scan = new Scanner(System.in);
            String input = "";
            int selection = 0;

            displayOptions();
            do {
                input = scan.next();
                selection = Integer.parseInt(input);

                switch (selection) {
                    case 0:
                        return;
                    case 1:
                        System.out.println(numSeatsAvailable() + " seats is available now");
                        displayOptions();
                        break;
                    case 2:
                        System.out.println("Please enter how many seats you want to hold:");
                        int seatNums = Integer.valueOf(scan.next());
                        System.out.println("Please enter your email address:");
                        String email = scan.next();
                        SeatHold seatHold = findAndHoldSeats(seatNums, email);
                        if (seatHold != null) {
                            System.out.println("Congratulation, you have successful held the seats! The Id of your held seats is : " + seatHold.getSeatHoldId() + "\n Held seats: \n");
                            Utils.displaySeats(seatHold.getSeats());
                        }
                        displayOptions();
                        break;
                    case 3:
                        System.out.println("Please enter Id of your held seats:");
                        int seatHoldId = Integer.valueOf(scan.next());
                        System.out.println("Please provide your email address:");
                        String emailAddr = scan.next();
                        String confirmationCode = reserveSeats(seatHoldId, emailAddr);
                        if (confirmationCode != null) {
                            System.out.println("Congratulation, you have successful reserved the seats! Your confirmation code is : " + confirmationCode + "\n Reserved seats: \n");
                            Utils.displaySeats(seatReserveMap.get(confirmationCode).getSeats());
                        }
                        displayOptions();
                        break;
                    default:
                        System.out.println("Please select a valid option from menu:");
                        displayOptions();
                        break;
                }
            } while (selection != 0);
        } catch (Exception ex) {
            System.out.println("Your input is invalid, please re-select the option from menu");
            run();
        }
    }

    private void displayOptions() {
        System.out.println(" \n 0. Exit \n 1. Show the number of available seats \n 2. Hold seats \n 3. Reserve held seats");
        System.out.println("Please select your option:\n");
    }
}
