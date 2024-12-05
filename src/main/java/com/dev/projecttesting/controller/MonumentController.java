package com.dev.projecttesting.controller;

import com.dev.projecttesting.model.Monuments;
import com.dev.projecttesting.repository.MonumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monuments")
public class MonumentController {

    @Autowired
    private MonumentRepository monumentRepository;

    // Add a new monument
    @PostMapping("/add-monument")
    public ResponseEntity<Monuments> addMonument(@RequestBody Monuments monument) {
        Monuments savedMonument = monumentRepository.save(monument);
        return ResponseEntity.ok(savedMonument);
    }

    // Get all monuments
    @GetMapping("/display-monuments")
    public ResponseEntity<List<Monuments>> getAllMonuments() {
        List<Monuments> monuments = monumentRepository.findAll();
        return ResponseEntity.ok(monuments);
    }

    // Get a monument by ID
    @GetMapping("/monument/{id}")
    public ResponseEntity<Monuments> getMonumentById(@PathVariable Long id) {
        return monumentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a monument
    @PutMapping("/update/{id}")
    public ResponseEntity<Monuments> updateMonument(@PathVariable Long id, @RequestBody Monuments updatedMonument) {
        return monumentRepository.findById(id).map(monument -> {
            monument.setTitle(updatedMonument.getTitle());
            monument.setDescription(updatedMonument.getDescription());
            monument.setYear(updatedMonument.getYear());
            monument.setImageUrl(updatedMonument.getImageUrl());
            Monuments savedMonument = monumentRepository.save(monument);
            return ResponseEntity.ok(savedMonument);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete a monument by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMonument(@PathVariable Long id) {
        monumentRepository.deleteById(id);
        return ResponseEntity.ok("Monument deleted successfully!");
    }
}
