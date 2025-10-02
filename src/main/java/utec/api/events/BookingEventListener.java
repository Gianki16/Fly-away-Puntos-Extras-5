package utec.api.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import utec.api.service.EmailService;


@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {

    private final EmailService emailService;

    @EventListener
    @Async("taskExecutor")
    public void handleBookingEvent(BookingEvent event) {
        log.info("Processing booking event for booking ID: {}", event.getBooking().getId());
        emailService.sendBookingConfirmationEmail(event.getBooking());
    }
}

