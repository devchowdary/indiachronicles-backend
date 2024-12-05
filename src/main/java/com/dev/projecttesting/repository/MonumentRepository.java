package com.dev.projecttesting.repository;

import com.dev.projecttesting.model.Monuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonumentRepository extends JpaRepository<Monuments, Long> {
}

