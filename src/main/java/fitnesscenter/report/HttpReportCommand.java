package fitnesscenter.report;

import rx.Observable;

import java.util.List;
import java.util.Map;

public interface HttpReportCommand {
    public <T> Observable<String> addEvent(Map<String, List<String>> parameters);
}
