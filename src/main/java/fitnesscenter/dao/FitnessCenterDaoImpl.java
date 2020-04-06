package fitnesscenter.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import fitnesscenter.dao.exceptions.TicketExpiredException;
import fitnesscenter.dao.exceptions.TicketUnexpiredException;
import fitnesscenter.dao.exceptions.UserNotFoundException;
import org.bson.Document;
import rx.Observable;

import java.time.LocalDateTime;


public class FitnessCenterDaoImpl implements FitnessCenterDao {
    private final MongoCollection<Document> tickets;
    private final MongoCollection<Document> events;

    public FitnessCenterDaoImpl(MongoCollection<Document> tickets, MongoCollection<Document> events) {
        this.tickets = tickets;
        this.events = events;
    }

    @Override
    public Observable<Ticket> getTickets(String ticketId) {
        return getTicketsById(ticketId).sorted();
    }

    @Override
    public Observable<Ticket> getLastUpdateTicket(String ticketId) {
        return getTickets(ticketId).last();
    }

    @Override
    public Observable<Success> createTicket(String id, LocalDateTime ticketEnding) {
        return getTickets(id)
                .defaultIfEmpty(null).flatMap(ticket -> {
                    LocalDateTime curTime = LocalDateTime.now();
                    if (ticket == null || ticket.getTicketEnding().isBefore(curTime)) {
                        return tickets.insertOne(new Ticket(id, LocalDateTime.now(), ticketEnding).toDocument());
                    }
                    return Observable.error(new TicketUnexpiredException());
                });
    }

    @Override
    public Observable<Success> renewTicket(String id, LocalDateTime ticketEnding) {
        return getTickets(id)
                .isEmpty().flatMap(isEmpty -> {
                    if (isEmpty) {
                        return Observable.error(new UserNotFoundException());
                    }
                    return tickets.insertOne(new Ticket(id, LocalDateTime.now(), ticketEnding).toDocument());
                });
    }

    @Override
    public Observable<TurnstileEvent> getEvents() {
        return events.find().toObservable().map(TurnstileEvent::new);
    }

    @Override
    public Observable<Success> addEvent(TurnstileEvent event) {
        return getTickets(event.getTicketId())
                .defaultIfEmpty(null).flatMap(ticket -> {
                    if (ticket == null) {
                        return Observable.error(new UserNotFoundException());
                    }
                    System.out.println(ticket.getTicketEnding());
                    System.out.println(event.getTimestamp());
                    System.out.println(ticket.getTicketEnding().isBefore(event.getTimestamp()));
                    if (ticket.getTicketEnding().isBefore(event.getTimestamp())) {
                        return Observable.error(new TicketExpiredException());
                    }
                    return events.insertOne(event.toDocument());
                });
    }

    private Observable<Ticket> getTicketsById(String id) {
        return tickets.find(Filters.eq("id", id)).toObservable().map(Ticket::new);
    }
}
