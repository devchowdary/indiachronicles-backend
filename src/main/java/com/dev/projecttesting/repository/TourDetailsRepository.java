package com.dev.projecttesting.repository;

import com.dev.projecttesting.model.TourDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourDetailsRepository extends JpaRepository<TourDetails, Long> {
}
