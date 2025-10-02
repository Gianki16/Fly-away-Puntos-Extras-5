package utec.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightBookRequestDTO {

    @NotNull(message = "Flight ID is required")
    private Long flightId;
}

