package com.brian.ticketservice;

import com.brian.ticketservice.serviceImpl.TicketServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by brianzhang on 9/1/18.
 */
public class App {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //row = 10, column = 10
        TicketServiceImpl ticketService = new TicketServiceImpl(10, 10, 600);
        executorService.execute(ticketService);
        executorService.shutdown();
    }
}
