package com.dev.projecttesting.controller;

import com.dev.projecttesting.model.Booking;
import com.dev.projecttesting.model.TourDetails;
import com.dev.projecttesting.repository.BookingRepository;
import com.dev.projecttesting.service.BookingService;
import com.dev.projecttesting.service.OtpService;
import com.dev.projecttesting.service.TourDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tour-details")
public class TourDetailsController {

    @Autowired
    private TourDetailsService tourDetailsService;
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    OtpService otpService;


    // Get all tour details
    @GetMapping("/gettours")
    public List<TourDetails> getAllTourDetails() {
        return tourDetailsService.getAllTourDetails();
    }

    // Get tour details by id
    @GetMapping("respective-tour/{id}")
    public TourDetails getTourDetailsById(@PathVariable Long id) {
        return tourDetailsService.getTourDetailsById(id);
    }

    // Add new tour details
    @PostMapping("/create")
    public TourDetails createTourDetails(@RequestBody TourDetails tourDetails) {
        return tourDetailsService.saveTourDetails(tourDetails);
    }
    @PutMapping("/update/{id}")
    public TourDetails updateTourDetails(@PathVariable Long id, @RequestBody TourDetails updatedTourDetails) {
        return tourDetailsService.updateTourDetails(id, updatedTourDetails);
    }


    // Delete tour details by id
    @DeleteMapping("/{id}")
    public void deleteTourDetails(@PathVariable Long id) {
        tourDetailsService.deleteTourDetails(id);
    }


//    @PostMapping("/book-tour")
//    public ResponseEntity<String> bookTour(@RequestBody Booking booking) {
//        try {
//            // Save the booking
//            Booking savedBooking = bookingRepository.save(booking);
//            System.out.println("Booking received: " + booking.toString());
//
//            // Prepare the email content
//            String subject = "Booking Confirmation: " + savedBooking.getPackageType();
//            String body = "Dear " + savedBooking.getName() + ",\n\n" +
//                    "Thank you for booking with us! Here are your booking details:\n\n" +
//                    "Package: " + savedBooking.getPackageType() + "\n" +
//                    "Check-in Date: " + savedBooking.getCheckIn() + "\n" +
//                    "Check-out Date: " + savedBooking.getCheckOut() + "\n" +
//                    "Duration: " + savedBooking.getDuration() + " nights\n" +
//                    "Members: " + savedBooking.getMembers() + "\n\n" +
//                    "Total Bill: ₹" + savedBooking.getTotalBill() + "\n" +
//                    "GST: ₹" + savedBooking.getGst() + "\n" +
//                    "Total Amount: ₹" + savedBooking.getTotalAmount() + "\n\n" +
//                    "We look forward to hosting you!\n\n" +
//                    "Best regards,\n" +
//                    "The IndiaChronicles Team";
//
//            // Send the email
//            otpService.sendEmail(savedBooking.getEmail(), subject, body);
//
//            // Respond with success message
//            return ResponseEntity.ok("Booking successful! Confirmation email sent to " + savedBooking.getEmail());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Failed to process booking: " + e.getMessage());
//        }
//    }

    @PostMapping("/book-tour")
    public ResponseEntity<String> bookTour(@RequestBody Booking booking) {
        try {
            // Save the booking
            Booking savedBooking = bookingRepository.save(booking);
            System.out.println("Booking received: " + booking.toString());

            // Prepare the email content with a structured and invoice-like design
            String subject = "Booking Confirmation: " + savedBooking.getPackageType();

            String body = "<html><body style='font-family: Arial, sans-serif; color: #333; background-color: #f4f4f4; padding: 20px;'>" +
                    "<div style='max-width: 700px; margin: 0 auto; background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);'>" +
                    "<h2 style='text-align: center; color: #007bff;'>Booking Confirmation</h2>" +
                    "<p>Dear " + savedBooking.getName() + ",</p>" +
                    "<p style='text-align: center;'>Thank you for booking with us! Below are your booking details.</p>" +
                    "<hr style='border: 0; border-top: 2px solid #007bff; margin: 20px 0;'>" +
                    "<table style='width: 100%; border-collapse: collapse; margin-bottom: 20px;'>" +
                    "<tr><td style='padding: 10px; font-weight: bold;'>Hotel:</td><td style='padding: 10px;'>" + savedBooking.getHotelName() + "</td></tr>" +

                    "<tr><td style='padding: 10px; font-weight: bold;'>Package:</td><td style='padding: 10px;'>" + savedBooking.getPackageType() + "</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold;'>Check-in Date:</td><td style='padding: 10px;'>" + savedBooking.getCheckIn() + "</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold;'>Check-out Date:</td><td style='padding: 10px;'>" + savedBooking.getCheckOut() + "</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold;'>Duration:</td><td style='padding: 10px;'>" + savedBooking.getDuration() + " nights</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold;'>Members:</td><td style='padding: 10px;'>" + savedBooking.getMembers() + "</td></tr>" +
                    "</table>" +

                    "<hr style='border: 0; border-top: 2px solid #007bff; margin: 20px 0;'>" +

                    "<h3 style='color: #007bff;'>Payment Summary</h3>" +
                    "<table style='width: 100%; border-collapse: collapse; margin-bottom: 20px;'>" +
                    "<tr><td style='padding: 10px; font-weight: bold;'>Total Bill:</td><td style='padding: 10px;'>₹" + savedBooking.getTotalBill() + "</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold;'>GST (₹):</td><td style='padding: 10px;'>₹" + savedBooking.getGst() + "</td></tr>" +
                    "<tr><td style='padding: 10px; font-weight: bold;'>Total Amount (₹):</td><td style='padding: 10px;'>₹" + savedBooking.getTotalAmount() + "</td></tr>" +
                    "</table>" +

                    "<hr style='border: 0; border-top: 2px solid #007bff; margin: 20px 0;'>" +

                    "<p style='text-align: center;'>We look forward to hosting you! If you have any questions, feel free to contact us.</p>" +
                    "<p style='text-align: center;'>Best regards,<br>The IndiaChronicles Team</p>" +
                    "</div>" +
                    "</body></html>";

            // Send the email with HTML content
            otpService.sendEmails(savedBooking.getEmail(), subject, body);

            // Respond with success message
            return ResponseEntity.ok("Booking successful! Confirmation email sent to " + savedBooking.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to process booking: " + e.getMessage());
        }
    }

    
    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // Endpoint to get booking by id
    @GetMapping("/bookings/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }




}
