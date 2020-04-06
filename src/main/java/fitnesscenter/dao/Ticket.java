package fitnesscenter.dao;

import org.bson.Document;

import java.time.LocalDateTime;

public class Ticket implements Comparable<Ticket> {

    private final String id;
    private final LocalDateTime timestamp;
    private final LocalDateTime ticketEnding;

    public Ticket(String id, LocalDateTime timestamp, LocalDateTime ticketEnding) {
        this.id = id;
        this.timestamp = timestamp;
        this.ticketEnding = ticketEnding;
    }

    public Ticket(Document document) {
        this.id = document.getString("id");
        this.timestamp = LocalDateTime.parse(document.getString("timestamp"));
        this.ticketEnding = LocalDateTime.parse(document.getString("ticketEnding"));
    }

    public Document toDocument() {
        return new Document()
                .append("id", id)
                .append("timestamp", timestamp.toString())
                .append("ticketEnding", ticketEnding.toString());
    }

    public LocalDateTime getTicketEnding() {
        return ticketEnding;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(Ticket o) {
        return this.timestamp.compareTo(o.timestamp);
    }

    @Override
    public String toString() {
        return "id : " + id + ", timestamp : " + timestamp.toString() + ", ticketEnding : " + ticketEnding.toString();
    }
}
