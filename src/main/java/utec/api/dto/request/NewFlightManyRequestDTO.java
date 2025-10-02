package utec.api.dto.request;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class NewFlightManyRequestDTO {

    @Valid
    private List<NewFlightRequestDTO> flights;
}
