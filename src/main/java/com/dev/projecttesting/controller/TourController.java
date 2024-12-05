package com.dev.projecttesting.controller;

import com.dev.projecttesting.model.Booking;
import com.dev.projecttesting.model.Tour;
import com.dev.projecttesting.repository.BookingRepository;
import com.dev.projecttesting.service.ToursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:9618")
@RequestMapping("/tours")
public class TourController {

    @Autowired
    ToursService toursService;

    @Autowired
    BookingRepository bookingRepository;

    @PostMapping("/addtour")
    public Tour addTour(@RequestBody Tour tour) {
        System.out.println(tour);
        return toursService.saveTour(tour);
    }

    @GetMapping("/viewtours")
    public List<Tour> getTours() {
        return toursService.getTours();
    }




}
