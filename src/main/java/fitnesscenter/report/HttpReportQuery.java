package fitnesscenter.report;

import rx.Observable;

import java.util.List;
import java.util.Map;

public interface HttpReportQuery {
    public <T> Observable<String> getTicketReport(Map<String, List<String>> parameters);

    public <T> Observable<String> getTotalReport(Map<String, List<String>> parameters);
}
