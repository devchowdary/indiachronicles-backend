package com.dev.projecttesting.repository;

import com.dev.projecttesting.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour,Long> {
}
