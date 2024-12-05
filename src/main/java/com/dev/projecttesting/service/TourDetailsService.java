package com.dev.projecttesting.service;

import com.dev.projecttesting.model.TourDetails;
import com.dev.projecttesting.repository.TourDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourDetailsService {

    @Autowired
    private TourDetailsRepository tourDetailsRepository;

    public List<TourDetails> getAllTourDetails() {
        return tourDetailsRepository.findAll();
    }

    public TourDetails getTourDetailsById(Long id) {
        return tourDetailsRepository.findById(id).orElse(null);
    }

    public TourDetails saveTourDetails(TourDetails tourDetails) {
        return tourDetailsRepository.save(tourDetails);
    }

    public TourDetails updateTourDetails(Long id, TourDetails updatedTourDetails) {
        TourDetails existingTour = tourDetailsRepository.findById(id).orElse(null);
        if (existingTour != null) {
            existingTour.setTitle(updatedTourDetails.getTitle());
            existingTour.setDescription(updatedTourDetails.getDescription());
            existingTour.setLocation(updatedTourDetails.getLocation());
            existingTour.setRating(updatedTourDetails.getRating());
            existingTour.setBestTime(updatedTourDetails.getBestTime());
            existingTour.setImages(updatedTourDetails.getImages());
            return tourDetailsRepository.save(existingTour);
        }
        return null;
    }


    public void deleteTourDetails(Long id) {
        tourDetailsRepository.deleteById(id);
    }
}