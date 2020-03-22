package reactive.reactive_mongo_driver;

import org.bson.Document;

public class Product {

    private final String id;
    private final Double ruPrice;


    public Product(String id, String price, String currency) {
        this.id = id;
        this.ruPrice = Currency.convertToRubles(currency, Double.parseDouble(price));
    }

    public Product(Document doc) {
        this.id = doc.getString("id");
        this.ruPrice = Double.parseDouble(doc.getString("ru-price"));
    }

    public String getId() {
        return id;
    }

    public String getPrice(String currency) {
        return Currency.convertFromRubles(currency, ruPrice).toString();
    }

    public Document toDocument() {
        return new Document()
                .append("id", id)
                .append("ru-price", Double.toString(ruPrice));
    }

    @Override
    public String toString() {
        return  "Product " + "\"" + id + "\"";
    }
}