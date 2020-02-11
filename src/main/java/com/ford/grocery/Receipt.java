package com.ford.grocery;

public class Receipt {

    private double total;

    public double getTotal() {
        return total;
    }

    public void addItems(final int quantity, final StockItem stockItem) {
        total = total + stockItem.getCost() * quantity;
    }

}
