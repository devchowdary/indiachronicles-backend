package com.dev.projecttesting.service;

import com.dev.projecttesting.model.Hotel;
import com.dev.projecttesting.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel addHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, Hotel updatedHotel) {
        return hotelRepository.findById(id).map(hotel -> {
            hotel.setName(updatedHotel.getName());
            hotel.setPackageType(updatedHotel.getPackageType());
            hotel.setPrice(updatedHotel.getPrice());
            hotel.setFood(updatedHotel.getFood());
            hotel.setRoom(updatedHotel.getRoom());
            hotel.setBed(updatedHotel.getBed());
            hotel.setWifi(updatedHotel.getWifi());
            hotel.setTv(updatedHotel.getTv());
            hotel.setImages(updatedHotel.getImages());
            return hotelRepository.save(hotel);
        }).orElseThrow(() -> new RuntimeException("Hotel not found with ID: " + id));
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }
}
