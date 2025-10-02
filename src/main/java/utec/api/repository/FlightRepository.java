package utec.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utec.api.domain.Flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumber(String flightNumber);

    Optional<Flight> findByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE " +
            "(:flightNumber IS NULL OR LOWER(f.flightNumber) LIKE LOWER(CONCAT('%', :flightNumber, '%'))) AND " +
            "(:airlineName IS NULL OR LOWER(f.airlineName) LIKE LOWER(CONCAT('%', :airlineName, '%'))) AND " +
            "(:estDepartureTimeFrom IS NULL OR f.estDepartureTime >= :estDepartureTimeFrom) AND " +
            "(:estDepartureTimeTo IS NULL OR f.estDepartureTime <= :estDepartureTimeTo)")
    List<Flight> searchFlights(@Param("flightNumber") String flightNumber,
                               @Param("airlineName") String airlineName,
                               @Param("estDepartureTimeFrom") LocalDateTime estDepartureTimeFrom,
                               @Param("estDepartureTimeTo") LocalDateTime estDepartureTimeTo);
}

