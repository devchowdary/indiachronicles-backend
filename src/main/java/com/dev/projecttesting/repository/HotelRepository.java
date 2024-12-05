package com.dev.projecttesting.repository;

import com.dev.projecttesting.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
