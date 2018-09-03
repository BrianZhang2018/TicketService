package com.brian.ticketservice.model;

/**
 * Created by brianzhang on 9/1/18.
 */
public class Seat {

    private int row;
    private int number;
    private int id;

    public Seat(int row, int number, int id) {
        this.row = row;
        this.number = number;
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    public int getId() {
        return id;
    }
}
