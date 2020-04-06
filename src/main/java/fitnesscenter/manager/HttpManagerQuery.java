package fitnesscenter.manager;

import rx.Observable;

import java.util.List;
import java.util.Map;

public interface HttpManagerQuery {
    public <T> Observable<String> getTicket(Map<String, List<String>> parameters);
}
