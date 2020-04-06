package fitnesscenter.manager;

import com.mongodb.rx.client.Success;
import fitnesscenter.dao.FitnessCenterDao;
import rx.Observable;

import java.util.List;
import java.util.Map;

import static fitnesscenter.utils.HttpUtils.getLocalDateTimeParameters;
import static fitnesscenter.utils.HttpUtils.getStringParameters;

public class HttpManagerCommandImpl implements HttpManagerCommand {
    private final FitnessCenterDao fitnessCenterDao;

    public HttpManagerCommandImpl(FitnessCenterDao fitnessCenterDao) {
        this.fitnessCenterDao = fitnessCenterDao;
    }

    public <T> Observable<String> createTicket(Map<String, List<String>> parameters) {
        return fitnessCenterDao.createTicket(getStringParameters(parameters, "id"), getLocalDateTimeParameters(parameters, "ticketEnding"))
                .map(Success::toString).onErrorReturn(Throwable::toString);
    }

    public <T> Observable<String> renewTicket(Map<String, List<String>> parameters) {
        return fitnessCenterDao.renewTicket(getStringParameters(parameters, "id"), getLocalDateTimeParameters(parameters, "ticketEnding"))
                .map(Success::toString).onErrorReturn(Throwable::toString);
    }
}
