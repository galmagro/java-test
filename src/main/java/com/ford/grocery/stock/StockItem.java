package com.ford.grocery.stock;

public class StockItem {

    private String product;

    private ItemUnitType unit;

    private double cost;

    public StockItem() {
    }

    public StockItem(final String product, final ItemUnitType unit, final double cost) {
        this.product = product;
        this.unit = unit;
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(final String product) {
        this.product = product;
    }

    public ItemUnitType getUnit() {
        return unit;
    }

    public void setUnit(final ItemUnitType unit) {
        this.unit = unit;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(final double cost) {
        this.cost = cost;
    }
}
