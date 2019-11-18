package jp.co.systena.tigerscave.webcart.model;

public class Item {

  private int itemId;
  private String name;
  private int price;

  public Item() {
  }

  public Item(int i, String n, int p) {
    this.itemId = i;
    this.name = n;
    this.price = p;
  }

  public int getItemId() {
    return itemId;
  }

  public void setItemId(int itemId) {
    this.itemId = itemId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }
}
