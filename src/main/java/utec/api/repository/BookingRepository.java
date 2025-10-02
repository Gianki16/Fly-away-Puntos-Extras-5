package utec.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utec.api.domain.Booking;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    @Query("SELECT b FROM Booking b JOIN b.flight f WHERE b.user.id = :userId AND " +
            "((f.estDepartureTime BETWEEN :departureStart AND :departureEnd) OR " +
            "(f.estArrivalTime BETWEEN :arrivalStart AND :arrivalEnd) OR " +
            "(:departureStart BETWEEN f.estDepartureTime AND f.estArrivalTime) OR " +
            "(:arrivalEnd BETWEEN f.estDepartureTime AND f.estArrivalTime))")
    List<Booking> findOverlappingBookings(@Param("userId") Long userId,
                                          @Param("departureStart") LocalDateTime departureStart,
                                          @Param("departureEnd") LocalDateTime departureEnd,
                                          @Param("arrivalStart") LocalDateTime arrivalStart,
                                          @Param("arrivalEnd") LocalDateTime arrivalEnd);
}

