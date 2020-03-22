package actor;

import org.json.JSONObject;

public class Response {

    private Searcher searcher;
    private JSONObject jsonObject;

    Response(Searcher s, JSONObject obj) {
        searcher = s;
        jsonObject = obj;
    }

    Searcher getSearcher() {
        return searcher;
    }

    JSONObject getJsonObject() {
        return jsonObject;
    }
}