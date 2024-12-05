package com.dev.projecttesting.service;

import com.dev.projecttesting.model.Tour;
import com.dev.projecttesting.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToursService
{
    @Autowired
    TourRepository tourRepository;

    public Tour saveTour(Tour tour)
    {
        System.out.println("Saving tours: " + tour);

        return tourRepository.save(tour);
    }

    public List<Tour> getTours()
    {
        return tourRepository.findAll();
    }

}
