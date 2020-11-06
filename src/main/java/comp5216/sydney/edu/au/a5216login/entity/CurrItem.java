package comp5216.sydney.edu.au.a5216login.entity;


import java.text.ParseException;

public class CurrItem {
    private String name, price, quantity;

    public CurrItem(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name +
                ";" + price +
                ";" + quantity;
    }

    public static CurrItem getObjectFromLine(String line) throws ParseException {
        String[] fields = line.split(";");
        return new CurrItem(fields[0],fields[1],fields[2]);
    }
}
