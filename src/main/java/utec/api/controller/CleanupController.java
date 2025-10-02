package utec.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utec.api.service.BookingService;
import utec.api.service.FlightService;
import utec.api.service.UserService;


@RestController
@RequestMapping("/cleanup")
@RequiredArgsConstructor
public class CleanupController {

    private final UserService userService;
    private final FlightService flightService;
    private final BookingService bookingService;

    @DeleteMapping
    public ResponseEntity<Void> cleanup() {
        bookingService.deleteAll();
        flightService.deleteAll();
        userService.deleteAll();
        return ResponseEntity.ok().build();
    }
}

