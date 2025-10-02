package utec.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({"id", "bookingDate", "flightId", "flightNumber", "customerId", "customerFirstName", "customerLastName"})
public class FlightBookingDetailDTO {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookingDate;

    private Long flightId;
    private String flightNumber;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
}

