package fitnesscenter.report;

import rx.Observable;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static fitnesscenter.utils.HttpUtils.*;

public class HttpReportCommandImpl implements HttpReportCommand{
    private final Storage storage;

    public HttpReportCommandImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public <T> Observable<String> addEvent(Map<String, List<String>> parameters) {
        String id = getStringParameters(parameters, "id");
        Duration visitDuration = getDurationParam(parameters, "duration");
        TicketReport report = storage.getOrDefault(id, new TicketReport());
        storage.put(id, report.addVisit(visitDuration));

        return Observable.just("Visit added by id=" + id);
    }

}
