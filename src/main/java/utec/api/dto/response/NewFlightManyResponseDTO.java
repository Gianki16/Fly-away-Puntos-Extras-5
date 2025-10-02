package utec.api.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class NewFlightManyResponseDTO {
    private List<NewIdDTO> createdFlights;
    private String message;
}

