package actor;

public class Request {
    private Searcher searcher;
    private String sentence;

    Request(Searcher s, String sent) {
        searcher = s;
        sentence = sent;
    }

    Searcher getSearcher() {
        return searcher;
    }

    String getSentence() {
        return sentence;
    }
}
