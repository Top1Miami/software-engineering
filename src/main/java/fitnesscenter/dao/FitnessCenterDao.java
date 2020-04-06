package fitnesscenter.dao;

import com.mongodb.rx.client.Success;
import rx.Observable;

import java.time.LocalDateTime;

public interface FitnessCenterDao {
    Observable<Ticket> getTickets(String ticketId);

    Observable<Ticket> getLastUpdateTicket(String ticketId);

    Observable<Success> createTicket(String id, LocalDateTime ticketEnding);

    Observable<Success> renewTicket(String id, LocalDateTime ticketEnding);

    Observable<TurnstileEvent> getEvents();

    Observable<Success> addEvent(TurnstileEvent event);
}
