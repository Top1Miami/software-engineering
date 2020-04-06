package fitnesscenter.report;

import rx.Observable;

import java.util.List;
import java.util.Map;

import static fitnesscenter.utils.HttpUtils.getStringParameters;

public class HttpReportQueryImpl implements HttpReportQuery {
    private final Storage storage;

    public HttpReportQueryImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public <T> Observable<String> getTicketReport(Map<String, List<String>> parameters) {
        String id = getStringParameters(parameters, "id");

        if (storage.containsKey(id)) {
            return Observable.just(storage.get(id).toString());
        } else {
            return Observable.just("No ticket with such id : " + id);
        }
    }

    @Override
    public <T> Observable<String> getTotalReport(Map<String, List<String>> parameters) {
        TicketReport totalReport = new TicketReport();
        for (TicketReport report : storage.values()) {
            totalReport = totalReport.mergeReports(report);
        }
        return Observable.just(totalReport.toString());
    }
}
