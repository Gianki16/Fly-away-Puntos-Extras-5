package utec.api.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import utec.api.domain.Booking;


@Getter
public class BookingEvent extends ApplicationEvent {

    private final Booking booking;

    public BookingEvent(Object source, Booking booking) {
        super(source);
        this.booking = booking;
    }
}
