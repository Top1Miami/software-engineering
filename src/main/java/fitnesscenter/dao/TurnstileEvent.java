package fitnesscenter.dao;

import org.bson.Document;

import java.time.LocalDateTime;

public class TurnstileEvent implements Comparable<TurnstileEvent> {
    private final EventType eventType;
    private final String ticketId;
    private final LocalDateTime timestamp;

    public TurnstileEvent(String ticketId, EventType eventType, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.ticketId = ticketId;
        this.timestamp = timestamp;
    }

    public TurnstileEvent(Document document) {
        this.ticketId = document.getString("id");
        this.timestamp = LocalDateTime.parse(document.getString("timestamp"));
        this.eventType = EventType.valueOf(document.getString("eventType"));
    }

    public Document toDocument() {
        return new Document()
                .append("id", ticketId)
                .append("timestamp", timestamp.toString())
                .append("eventType", eventType.toString());
    }

    @Override
    public int compareTo(TurnstileEvent o) {
        int idComparision = this.ticketId.compareTo(o.ticketId);
        if (idComparision != 0) {
            return idComparision;
        }
        return this.timestamp.compareTo(o.timestamp);
    }

    public String getTicketId() {
        return ticketId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "id : " + ticketId + ", timestamp : " + timestamp.toString() + ", eventType : " + eventType.toString();
    }
}
