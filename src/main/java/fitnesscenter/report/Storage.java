package fitnesscenter.report;

import fitnesscenter.dao.EventType;
import fitnesscenter.dao.FitnessCenterDao;
import fitnesscenter.dao.TurnstileEvent;
import rx.Observable;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private final Map<String, TicketReport> reports = new HashMap<>();

    public Storage(FitnessCenterDao fitnessCenterDao) {
        fitnessCenterDao.getEvents()
                .toSortedList()
                .flatMap(events -> {
                    TurnstileEvent previousEvent = null;
                    for (TurnstileEvent event : events) {
                        if (event.getEventType() == EventType.ENTER) {
                            previousEvent = event;
                        } else {
                            TicketReport report = reports.getOrDefault(event.getTicketId(), new TicketReport());
                            reports.put(event.getTicketId(), report.addVisit(Duration.between(previousEvent.getTimestamp(), event.getTimestamp())));
                        }
                    }
                    return Observable.empty();
                })
                .subscribe();
    }

    public void put(String id, TicketReport report) {
        reports.put(id, report);
    }

    public TicketReport get(String id) {
        return reports.get(id);
    }

    public TicketReport getOrDefault(String id, TicketReport defaultValue) {
        return reports.getOrDefault(id, defaultValue);
    }

    public Collection<TicketReport> values() {
        return reports.values();
    }

    public boolean containsKey(String id) {
        return reports.containsKey(id);
    }
}
