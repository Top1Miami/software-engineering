package fitnesscenter.turnstile;

import com.mongodb.rx.client.Success;
import fitnesscenter.dao.EventType;
import fitnesscenter.dao.FitnessCenterDao;
import fitnesscenter.dao.TurnstileEvent;
import rx.Observable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static fitnesscenter.utils.HttpUtils.getStringParameters;
import static fitnesscenter.utils.HttpUtils.getLocalDateTimeParameters;

public class HttpTurnstileCommand implements HttpTurnstile {
    private final FitnessCenterDao fitnessCenterDao;

    public HttpTurnstileCommand(FitnessCenterDao fitnessCenterDao) {
        this.fitnessCenterDao = fitnessCenterDao;
    }

    @Override
    public <T> Observable<String> enter(Map<String, List<String>> parameters) {
        String id = getStringParameters(parameters, "id");
        LocalDateTime timestamp = getLocalDateTimeParameters(parameters, "timestamp");

        return fitnessCenterDao
                .addEvent(new TurnstileEvent(id, EventType.ENTER, timestamp))
                .map(Success::toString)
                .onErrorReturn(Throwable::toString);
    }

    @Override
    public <T> Observable<String> exit(Map<String, List<String>> parameters) {
        String id = getStringParameters(parameters, "id");
        LocalDateTime timestamp = getLocalDateTimeParameters(parameters, "timestamp");

        return fitnessCenterDao
                .addEvent(new TurnstileEvent(id, EventType.EXIT, timestamp))
                .map(Success::toString)
                .onErrorReturn(Throwable::toString);
    }
}
