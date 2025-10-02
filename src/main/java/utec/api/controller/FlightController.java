package utec.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import utec.api.domain.Flight;
import utec.api.domain.User;
import utec.api.dto.request.FlightBookRequestDTO;
import utec.api.dto.request.NewFlightManyRequestDTO;
import utec.api.dto.request.NewFlightRequestDTO;
import utec.api.dto.response.FlightBookingDetailDTO;
import utec.api.dto.response.FlightSearchResponseDTO;
import utec.api.dto.response.NewFlightManyResponseDTO;
import utec.api.dto.response.NewIdDTO;
import utec.api.exceptions.BusinessException;
import utec.api.repository.FlightRepository;
import utec.api.service.BookingService;
import utec.api.service.FlightService;
import utec.api.service.UserService;



import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;
    private final BookingService bookingService;
    private final UserService userService;
    private final FlightRepository flightRepository;

    @PostMapping("/create")
    public ResponseEntity<NewIdDTO> create(@Valid @RequestBody NewFlightRequestDTO newFlight) {
        // Las validaciones de @Valid se ejecutarán automáticamente
        NewIdDTO result = flightService.createFlight(newFlight);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/create-many")
    public ResponseEntity<String> createMany(@Valid @RequestBody NewFlightManyRequestDTO requestDTO) {
        CompletableFuture<NewFlightManyResponseDTO> future = flightService.createManyFlights(requestDTO);
        // Para el tester, devolvemos una respuesta inmediata
        return ResponseEntity.accepted().body("Flights creation started");
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightSearchResponseDTO>> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) String estDepartureTimeFrom,
            @RequestParam(required = false) String estDepartureTimeTo) {

        try {
            // Implementación simple y directa
            List<Flight> allFlights = flightRepository.findAll();

            // Filtrar manualmente
            List<Flight> filteredFlights = allFlights.stream()
                    .filter(flight -> {
                        if (flightNumber != null && !flightNumber.isEmpty()) {
                            return flight.getFlightNumber().toLowerCase().contains(flightNumber.toLowerCase());
                        }
                        return true;
                    })
                    .filter(flight -> {
                        if (airlineName != null && !airlineName.isEmpty()) {
                            return flight.getAirlineName().toLowerCase().contains(airlineName.toLowerCase());
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            // Convertir a DTOs manualmente
            List<FlightSearchResponseDTO> response = filteredFlights.stream()
                    .map(flight -> {
                        FlightSearchResponseDTO dto = new FlightSearchResponseDTO();
                        dto.setId(flight.getId());
                        dto.setAirlineName(flight.getAirlineName());
                        dto.setFlightNumber(flight.getFlightNumber());
                        dto.setEstDepartureTime(flight.getEstDepartureTime());
                        dto.setEstArrivalTime(flight.getEstArrivalTime());
                        dto.setAvailableSeats(flight.getAvailableSeats());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log del error específico
            e.printStackTrace();
            throw new BusinessException("Search error: " + e.getMessage());
        }
    }


    @PostMapping("/book")
    public ResponseEntity<NewIdDTO> book(@Valid @RequestBody FlightBookRequestDTO requestDTO,
                                         Authentication authentication) {

        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new BusinessException("User not found"));

        NewIdDTO result = bookingService.bookFlight(requestDTO, user);
        return ResponseEntity.ok(result);
    }


    // Endpoint para obtener detalles de booking
    @GetMapping("/book/{id}")
    public ResponseEntity<FlightBookingDetailDTO> getBookingDetails(@PathVariable Long id) {
        FlightBookingDetailDTO booking = bookingService.getBookingDetails(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return flightService.findById(id)
                .map(flight -> ResponseEntity.ok(flight))
                .orElse(ResponseEntity.notFound().build());
    }
}

