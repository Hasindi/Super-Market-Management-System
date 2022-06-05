package model;

import java.math.BigDecimal;

public class ItemDTO {
    private String ItemCode;
    private String Description ;
    private String PackSize;
    private BigDecimal UnitPrice;
    private int QtyOnHand;


    public ItemDTO() {
    }

    public ItemDTO(String itemCode, String description, String packSize, BigDecimal unitPrice, int qtyOnHand) {
        ItemCode = itemCode;
        Description = description;
        PackSize = packSize;
        UnitPrice = unitPrice;
        QtyOnHand = qtyOnHand;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPackSize() {
        return PackSize;
    }

    public void setPackSize(String packSize) {
        PackSize = packSize;
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        UnitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return QtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        QtyOnHand = qtyOnHand;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "ItemCode='" + ItemCode + '\'' +
                ", Description='" + Description + '\'' +
                ", PackSize='" + PackSize + '\'' +
                ", UnitPrice=" + UnitPrice +
                ", QtyOnHand=" + QtyOnHand +
                '}';
    }
}