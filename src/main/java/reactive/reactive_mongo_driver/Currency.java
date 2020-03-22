package reactive.reactive_mongo_driver;

import java.util.HashMap;
import java.util.Map;

public class Currency {

    private final static Map<String, Double> currencies = new HashMap<String, Double>() {{
        put("RUB", 1.0);
        put("USD", 79.98);
        put("EUR", 86.08);
    }};

    public static Boolean correct(String cur) {
        return currencies.get(cur) != null;
    }

    public static Double convertToRubles(String cur, Double val) {
        return val * currencies.get(cur);
    }

    public static Double convertFromRubles(String cur, Double val) {
        return val / currencies.get(cur);
    }

}