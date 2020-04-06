package fitnesscenter.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    public static <T> String getStringParameters(Map<String, List<String>> parameters, String parameter) {
        return parameters.get(parameter).get(0);
    }

    public static <T> LocalDateTime getLocalDateTimeParameters(Map<String, List<String>> parameters, String parameter) {
        return LocalDateTime.parse(getStringParameters(parameters, parameter));
    }

    public static <T> Duration getDurationParam(Map<String, List<String>> parameters, String parameter) {
        return Duration.parse(getStringParameters(parameters, parameter));
    }

}
