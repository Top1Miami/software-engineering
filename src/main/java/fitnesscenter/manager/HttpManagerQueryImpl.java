package fitnesscenter.manager;

import fitnesscenter.dao.FitnessCenterDao;
import fitnesscenter.dao.Ticket;
import rx.Observable;

import java.util.List;
import java.util.Map;

import static fitnesscenter.utils.HttpUtils.getStringParameters;

public class HttpManagerQueryImpl implements HttpManagerQuery {
    private final FitnessCenterDao fitnessCenterDao;

    public HttpManagerQueryImpl(FitnessCenterDao fitnessCenterDao) {
        this.fitnessCenterDao = fitnessCenterDao;
    }


    public <T> Observable<String> getTicket(Map<String, List<String>> parameters) {
        String id = getStringParameters(parameters, "id");

        return fitnessCenterDao.getLastUpdateTicket(id).map(Ticket::toString);
    }
}
