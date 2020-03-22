package reactive.netty_http_server;

import io.netty.handler.codec.http.QueryStringDecoder;
import io.reactivex.netty.protocol.http.server.HttpServer;
import reactive.reactive_mongo_driver.Currency;
import reactive.reactive_mongo_driver.Product;
import reactive.reactive_mongo_driver.ReactiveMongoDriver;
import reactive.reactive_mongo_driver.User;
import rx.Observable;

import java.util.List;
import java.util.Map;

public class RxNettyHttpServer {

    public static void main(final String[] args) {
        HttpServer
                .newServer(8080)
                .start((req, resp) -> {
                    String name = req.getDecodedPath().substring(1);
                    Map<String, List<String>> parameters = new QueryStringDecoder(req.getUri()).parameters();
                    Observable<String> response;
                    switch (name) {
                        case "register-user":
                            response = ReactiveMongoDriver
                                    .getUsers()
                                    .exists(user -> user.getId().equals(parameters.get("id").get(0)))
                                    .flatMap(exist -> {
                                        String userName = parameters.get("id").get(0);
                                        String currency = parameters.get("currency").get(0);
                                        if (exist) {
                                            return Observable
                                                    .just(userName +
                                                            " can't register user because he already exists.");
                                        } else if (!Currency.correct(currency)) {
                                            return Observable
                                                    .just(userName +
                                                            " can't register user because he has an incorrect currency: " +
                                                            currency + ".");
                                        } else {
                                            return ReactiveMongoDriver
                                                    .addUser(new User(
                                                            userName,
                                                            currency))
                                                    .map(success -> userName + " successfully registered.");
                                        }
                                    });
                            break;
                        case "add-product":
                            response = ReactiveMongoDriver
                                    .getProducts()
                                    .exists(product -> product.getId().equals(parameters.get("id").get(0)))
                                    .flatMap(exist -> {
                                        String id = parameters.get("id").get(0);
                                        String currency = parameters.get("currency").get(0);
                                        String price = parameters.get("price").get(0);
                                        if (exist) {
                                            return Observable
                                                    .just(id +
                                                            " can't add product because it already exists.");
                                        } else if (!Currency.correct(currency)) {
                                            return Observable.just(id +
                                                    " can't add product because its price has incorrect currency: " +
                                                    currency + ".");
                                        } else {
                                            return ReactiveMongoDriver
                                                    .addProduct(new Product(
                                                            id,
                                                            price,
                                                            currency))
                                                    .map(success -> id + " successfully added.");
                                        }
                                    });
                            break;
                        case "show-products":
                            response = ReactiveMongoDriver
                                    .getUsers()
                                    .filter(user -> user.getId().equals(parameters.get("id").get(0)))
                                    .defaultIfEmpty(new User(parameters.get("id").get(0), "UNDEF"))
                                    .flatMap(maybeUser -> {
                                        if (Currency.correct(maybeUser.getCurrency())) {
                                            return ReactiveMongoDriver
                                                    .getProducts()
                                                    .map(product ->
                                                            product.getId() + ": " +
                                                                    product.getPrice(maybeUser.getCurrency()) + " " +
                                                                    maybeUser.getCurrency() + "\n");
                                        } else {
                                            return Observable
                                                    .just(maybeUser)
                                                    .map(defaultUser ->
                                                            defaultUser.toString() + " doesn't exist.");
                                        }
                                    });
                            break;
                        default:
                            response = null;
                    }
                    return resp.writeString(response);
                })
                .awaitShutdown();
    }
}