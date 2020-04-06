package fitnesscenter.turnstile;

import rx.Observable;

import java.util.List;
import java.util.Map;

public interface HttpTurnstile {
    public <T> Observable<String> enter(Map<String, List<String>> parameters);

    public <T> Observable<String> exit(Map<String, List<String>> parameters);
}