package reactive.reactive_mongo_driver;


import org.bson.Document;

public class User {

    private final String id;
    private final String currency;

    public User(String id, String currency) {
        this.id = id;
        this.currency = currency;
    }

    public User(Document doc) {
        this.id = doc.getString("id");
        this.currency = doc.getString("currency");
    }

    public String getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public Document toDocument() {
        return new Document()
                .append("id", id)
                .append("currency", currency);
    }

    @Override
    public String toString() {
        return  "User " + "\"" + id + "\"";
    }
}