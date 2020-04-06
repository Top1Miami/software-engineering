package fitnesscenter;

import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;
import com.mongodb.rx.client.Success;
import fitnesscenter.dao.*;
import fitnesscenter.dao.exceptions.TicketExpiredException;
import fitnesscenter.dao.exceptions.UserNotFoundException;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class DaoAndManagerTest {

    private final MongoDatabase database;
    private FitnessCenterDao dao;

    public DaoAndManagerTest() {
        database = MongoClients.create("mongodb://localhost:27017").getDatabase("test-database");
    }

    @Before
    public void begin() {
        MongoCollection<Document> tickets = database.getCollection("tickets");
        MongoCollection<Document> events = database.getCollection("events");
        dao = new FitnessCenterDaoImpl(tickets, events);
    }

    @After
    public void end() {
        database.getCollection("tickets").drop().toBlocking().single();
        database.getCollection("events").drop().toBlocking().single();
    }

    @Test
    public void renewSubscription() {
        LocalDateTime now = LocalDateTime.parse("2020-04-06T00:33");
        LocalDateTime later = now.plusMinutes(10);
        dao.createTicket("0", now).toBlocking().single();
        dao.renewTicket("0", later).toBlocking().single();
        Ticket actualTicket = dao.getLastUpdateTicket("0").toBlocking().single();
        Assert.assertEquals("0", actualTicket.getId());
    }

    @Test
    public void renewNonExistingSubscription() {
        LocalDateTime now = LocalDateTime.parse("2020-04-06T00:33");
        LocalDateTime later = now.plusMinutes(10);
        Assert.assertEquals(new UserNotFoundException().toString(), dao
                .renewTicket("0", later)
                .map(Success::toString)
                .onErrorReturn(Throwable::toString)
                .toBlocking()
                .single()
        );
    }

    @Test
    public void addEvents() {
        LocalDateTime now = LocalDateTime.parse("2020-04-06T00:33");
        LocalDateTime later = now.plusMinutes(10);
        dao.createTicket("1337", later).toBlocking().single();
        System.out.println(dao.getLastUpdateTicket("1337").toBlocking().single().toString());
        TurnstileEvent event = new TurnstileEvent("1337", EventType.ENTER, now);
        dao.addEvent(event).toBlocking().single();
        Assert.assertEquals(event.toString(), dao
                .getEvents()
                .map(TurnstileEvent::toString)
                .toBlocking()
                .single()
        );
        System.out.println("secondTest");
        System.out.println(dao.getLastUpdateTicket("1337").toBlocking().single().toString());
        LocalDateTime laterPassed = later.plusMinutes(1337);
        event = new TurnstileEvent("1337", EventType.ENTER, laterPassed);
        System.out.println(event.toString());
        Assert.assertEquals(new TicketExpiredException().toString(), dao
                .addEvent(event)
                .map(Success::toString)
                .onErrorReturn(Throwable::toString)
                .toBlocking()
                .single()
        );
    }

    @Test
    public void addEventNoTicket() {
        LocalDateTime now = LocalDateTime.parse("2020-04-06T00:33");
        LocalDateTime later = now.plusMinutes(10);
        TurnstileEvent event = new TurnstileEvent("1337", EventType.ENTER, later);
        Assert.assertEquals(new UserNotFoundException().toString(), dao
                .addEvent(event)
                .map(Success::toString)
                .onErrorReturn(Throwable::toString)
                .toBlocking()
                .single()
        );
    }
}