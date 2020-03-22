package reactive;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

public class ReactiveTest {

    private String sendRequest(String requestStr) {
        try {
            URL url = new URL("http://localhost:8080/" + requestStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Assertions.assertEquals(connection.getResponseCode(), HttpURLConnection.HTTP_OK);
            StringBuilder responseStr = new StringBuilder();
            try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    responseStr.append(line);
                }
            }
            return responseStr.toString();
        } catch (IOException ex) {
            fail("Request isn't sent: " + ex.getMessage());
        }
        return "";
    }

    @Test
    void correctRequests() {
        String respStr;

        respStr = sendRequest("register-user?id=kek&currency=RUB");
        Assertions.assertTrue(respStr.contains("successfully"));

        respStr = sendRequest("add-product?id=grecha&price=100.0&currency=USD");
        Assertions.assertTrue(respStr.contains("successfully"));
        respStr = sendRequest("add-product?id=conservi&price=100.0&currency=EUR");
        Assertions.assertTrue(respStr.contains("successfully"));

        respStr = sendRequest("show-products?id=kek");
        Assertions.assertTrue(respStr.contains("grecha") && respStr.contains("conservi") &&
                respStr.contains("RUB") && !respStr.contains("USD") && !respStr.contains("EUR"));
    }

    @Test
    void incorrectRequests() {
        String respStr;

        respStr = sendRequest("register-user?id=kek&currency=USD");
        Assertions.assertTrue(respStr.contains("already exists"));
        respStr = sendRequest("register-user?id=roflocur&currency=CUR");
        Assertions.assertTrue(respStr.contains("incorrect currency"));

        respStr = sendRequest("add-product?id=grecha&price=1337.0&currency=USD");
        Assertions.assertTrue(respStr.contains("already exists"));

        respStr = sendRequest("show-products?id=roflo-user");
        Assertions.assertTrue(respStr.contains("doesn't exist"));;
    }
}