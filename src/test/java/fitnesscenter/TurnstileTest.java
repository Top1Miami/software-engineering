package fitnesscenter;

import com.mongodb.rx.client.Success;
import fitnesscenter.dao.FitnessCenterDao;
import fitnesscenter.turnstile.HttpTurnstileCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TurnstileTest {
    @Mock
    private FitnessCenterDao dao;

    private HttpTurnstileCommand turnstileServer;

    @Before
    public void begin() {
        MockitoAnnotations.initMocks(this);
        when(dao.addEvent(any()))
                .thenReturn(Observable.just(Success.SUCCESS));
        turnstileServer = new HttpTurnstileCommand(dao);
    }

    @Test
    public void testEnter() {
        LocalDateTime now = LocalDateTime.now();

        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("id", Collections.singletonList("0"));
        parameters.put("timestamp", Collections.singletonList(now.toString()));
        Assert.assertEquals("SUCCESS", turnstileServer.enter(parameters).toBlocking().single());
    }

    @Test
    public void testExit() {
        LocalDateTime now = LocalDateTime.now();

        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("id", Collections.singletonList("0"));
        parameters.put("timestamp", Collections.singletonList(now.toString()));
        turnstileServer.enter(parameters);

        parameters.put("timestamp", Collections.singletonList(now.plusMinutes(1).toString()));
        Assert.assertEquals("SUCCESS", turnstileServer.exit(parameters).toBlocking().single());
    }
}
