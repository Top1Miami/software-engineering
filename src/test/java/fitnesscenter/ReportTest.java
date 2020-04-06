package fitnesscenter;

import fitnesscenter.dao.FitnessCenterDao;
import fitnesscenter.report.HttpReportCommandImpl;
import fitnesscenter.report.HttpReportQueryImpl;
import fitnesscenter.report.Storage;
import fitnesscenter.report.TicketReport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

public class ReportTest {
    @Mock
    private FitnessCenterDao dao;

    private HttpReportQueryImpl httpReportQuery;
    private HttpReportCommandImpl httpReportCommand;

    @Before
    public void begin() {
        MockitoAnnotations.initMocks(this);
        when(dao.getEvents())
                .thenReturn(Observable.empty());
        Storage storage = new Storage(dao);
        httpReportCommand = new HttpReportCommandImpl(storage);
        httpReportQuery = new HttpReportQueryImpl(storage);

        addEvent("0", 1);
        addEvent("0", 3);
        addEvent("1", 1);
        addEvent("1", 2);
        addEvent("2", 1);
        addEvent("2", 1);
    }

    private void addEvent(String id, int durationHours) {
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("id", Collections.singletonList(id));
        parameters.put("duration", Collections.singletonList(Duration.ofHours(durationHours).toString()));
        httpReportCommand.addEvent(parameters);
    }

    @Test
    public void testGetSubscriptionReport() {
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("id", Collections.singletonList("0"));
        Assert.assertEquals(new TicketReport(2, Duration.ofHours(4)).toString(),
                httpReportQuery.getTicketReport(parameters).toBlocking().single());

        parameters.put("id", Collections.singletonList("1"));
        Assert.assertEquals(new TicketReport(2, Duration.ofHours(3)).toString(),
                httpReportQuery.getTicketReport(parameters).toBlocking().single());

        parameters.put("id", Collections.singletonList("2"));
        Assert.assertEquals(new TicketReport(2, Duration.ofHours(2)).toString(),
                httpReportQuery.getTicketReport(parameters).toBlocking().single());
    }

    @Test
    public void testGetTotalReport() {
        Map<String, List<String>> parameters = new HashMap<>();
        Assert.assertEquals(new TicketReport(6, Duration.ofHours(9)).toString(),
                httpReportQuery.getTotalReport(parameters).toBlocking().single());
    }
}
