package com.dev.projecttesting.repository;

import com.dev.projecttesting.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
