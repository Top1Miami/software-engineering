package reactive.reactive_mongo_driver;

import com.mongodb.rx.client.*;
import rx.Observable;
import org.bson.Document;

public class ReactiveMongoDriver {

    private static MongoClient client =  MongoClients.create("mongodb://localhost:27017");
    private static MongoDatabase database = client.getDatabase("shop");
    private static MongoCollection<Document> userDocs = database.getCollection("users");
    private static MongoCollection<Document> productDocs = database.getCollection("products");

    public static Observable<Success> addUser(User user){
        return userDocs.insertOne(user.toDocument());
    }

    public static Observable<Success> addProduct(Product product) {
        return productDocs.insertOne(product.toDocument());
    }

    public static Observable<User> getUsers() {
        return userDocs.find().toObservable().map(User::new);
    }

    public static Observable<Product> getProducts() {
        return productDocs.find().toObservable().map(Product::new);
    }
}