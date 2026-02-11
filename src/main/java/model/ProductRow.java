package model;

public class ProductRow {

    private final int id;
    private final String name;
    private final int price;
    private final int stock;
    private final String categoryName;

    public ProductRow(int id, String name, int price, int stock, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
    public String getCategoryName() { return categoryName; }
}