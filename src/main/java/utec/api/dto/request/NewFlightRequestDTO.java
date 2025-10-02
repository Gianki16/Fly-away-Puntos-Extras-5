package utec.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewFlightRequestDTO {

    @NotBlank(message = "Airline name is required")
    private String airlineName;

    @NotBlank(message = "Flight number is required")
    @Pattern(regexp = "^[A-Z]{2,3}[0-9]{3}$", message = "Flight number must match pattern: 2-3 letters + 3 numbers")
    private String flightNumber;

    @NotNull(message = "Estimated departure time is required")
    // No usar @JsonFormat, usar custom deserializer
    private LocalDateTime estDepartureTime;

    @NotNull(message = "Estimated arrival time is required")
    private LocalDateTime estArrivalTime;

    @NotNull(message = "Available seats is required")
    @Positive(message = "Available seats must be greater than 0")
    private Integer availableSeats;
}
