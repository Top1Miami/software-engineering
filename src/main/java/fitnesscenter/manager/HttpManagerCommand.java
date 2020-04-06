package fitnesscenter.manager;

import rx.Observable;

import java.util.List;
import java.util.Map;

public interface HttpManagerCommand {

    public <T> Observable<String> createTicket(Map<String, List<String>> parameters);

    public <T> Observable<String> renewTicket(Map<String, List<String>> parameters);
}
