package actor;

import org.json.JSONObject;

import java.util.Random;

import static java.lang.Thread.sleep;

public class StubServer {

    public static double hangProbability = 0;

    public static JSONObject getResponse(Request request) throws InterruptedException {
        if (new Random().nextDouble() < hangProbability) {
            sleep(1500);
        }
        return new JSONObject("{ \"result\" : answer from " +
                request.getSearcher().name() + " for " +
                request.getSentence() + "}");
    }
}