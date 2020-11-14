package shravan.nyshadh.billmaker;

public class Invoice {
    private String name, number, price, date;

    public Invoice(String name, String number, String price, String date) {
        this.name = name;
        this.number = number;
        this.price = price;
        this.date = date;
    }

    public Invoice() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
