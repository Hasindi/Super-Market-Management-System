package entity;

public class ItemQTYRates {
    private String itemCode;
    private int qty;

    public ItemQTYRates() {
    }

    public ItemQTYRates(String itemCode, int qty) {
        this.itemCode = itemCode;
        this.qty = qty;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
