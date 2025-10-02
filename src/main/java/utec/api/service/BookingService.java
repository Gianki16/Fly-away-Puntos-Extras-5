package utec.api.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utec.api.domain.Booking;
import utec.api.domain.Flight;
import utec.api.domain.User;
import utec.api.dto.request.FlightBookRequestDTO;
import utec.api.dto.response.FlightBookingDetailDTO;
import utec.api.dto.response.NewIdDTO;
import utec.api.events.BookingEvent;
import utec.api.exceptions.BusinessException;
import utec.api.repository.BookingRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightService flightService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public NewIdDTO bookFlight(FlightBookRequestDTO requestDTO, User user) {
        Flight flight = flightService.findById(requestDTO.getFlightId())
                .orElseThrow(() -> new BusinessException("Flight not found"));

        // Validaciones nice-to-have
        validateFlightBooking(flight, user);

        // Actualizar asientos disponibles
        flightService.updateAvailableSeats(flight.getId(), 1);

        // Crear booking
        Booking booking = new Booking();
        booking.setBookingDate(LocalDateTime.now());
        booking.setFlight(flight);
        booking.setUser(user);

        Booking savedBooking = bookingRepository.save(booking);

        // Publicar evento para envío de email
        BookingEvent event = new BookingEvent(this, savedBooking);
        eventPublisher.publishEvent(event);

        return new NewIdDTO(savedBooking.getId().toString());
    }

    @Transactional(readOnly = true)
    public FlightBookingDetailDTO getBookingDetails(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("Booking not found"));

        FlightBookingDetailDTO dto = new FlightBookingDetailDTO();
        dto.setId(booking.getId());
        dto.setBookingDate(booking.getBookingDate());
        dto.setFlightId(booking.getFlight().getId());
        dto.setFlightNumber(booking.getFlight().getFlightNumber());
        dto.setCustomerId(booking.getUser().getId());
        dto.setCustomerFirstName(booking.getUser().getFirstName());
        dto.setCustomerLastName(booking.getUser().getLastName());

        return dto;
    }


    private void validateFlightBooking(Flight flight, User user) {
        // Validar que el vuelo no esté en el pasado o en tránsito
        LocalDateTime now = LocalDateTime.now();
        if (flight.getEstDepartureTime().isBefore(now)) {
            throw new BusinessException("Cannot book a flight in the past or in transit");
        }

        // Validar que no haya conflictos de horario
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                user.getId(),
                flight.getEstDepartureTime(),
                flight.getEstDepartureTime(),
                flight.getEstArrivalTime(),
                flight.getEstArrivalTime()
        );

        if (!overlappingBookings.isEmpty()) {
            throw new BusinessException("You have overlapping flights");
        }
    }

    private FlightBookingDetailDTO mapToBookingDetail(Booking booking) {
        FlightBookingDetailDTO dto = new FlightBookingDetailDTO();
        dto.setId(booking.getId());
        dto.setBookingDate(booking.getBookingDate());
        dto.setFlightId(booking.getFlight().getId());
        dto.setFlightNumber(booking.getFlight().getFlightNumber());
        dto.setCustomerId(booking.getUser().getId());
        dto.setCustomerFirstName(booking.getUser().getFirstName());
        dto.setCustomerLastName(booking.getUser().getLastName());
        return dto;
    }

    public void deleteAll() {
        bookingRepository.deleteAll();
    }
}

