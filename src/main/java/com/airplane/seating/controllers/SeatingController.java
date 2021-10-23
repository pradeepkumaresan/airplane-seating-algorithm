package com.airplane.seating.controllers;

import com.airplane.seating.services.SeatingService;
import dto.MatrixItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SeatingController {
    @Autowired
    SeatingService seatingService;

    @GetMapping(value = "/seat")
    public MatrixItem[][] seat(
//            @RequestParam List<List<Integer>> seatingArrangement,
            @RequestParam Integer numberOfPassengers) {
        return seatingService.seat(numberOfPassengers);
    }
}
