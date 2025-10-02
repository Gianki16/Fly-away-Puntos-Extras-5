package utec.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import utec.api.domain.Booking;


import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Async("taskExecutor")
    public void sendBookingConfirmationEmail(Booking booking) {
        try {
            String emailContent = generateEmailContent(booking);
            String fileName = "flight_booking_email_" + booking.getId() + ".txt";

            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(emailContent);
                log.info("Booking confirmation email saved to file: {}", fileName);
            }
        } catch (IOException e) {
            log.error("Error saving booking confirmation email", e);
        }
    }

    private String generateEmailContent(Booking booking) {
        return String.format(
                "Hello %s %s,\n\n" +
                        "Your booking was successful!\n\n" +
                        "The booking is for flight %s with departure date of %s and arrival date of %s.\n\n" +
                        "The booking was registered at %s.\n\n" +
                        "Bon Voyage!\n" +
                        "Fly Away Travel\n",
                booking.getUser().getFirstName(),
                booking.getUser().getLastName(),
                booking.getFlight().getFlightNumber(),
                booking.getFlight().getEstDepartureTime().format(ISO_FORMATTER),
                booking.getFlight().getEstArrivalTime().format(ISO_FORMATTER),
                booking.getBookingDate().format(ISO_FORMATTER)
        );
    }
}

