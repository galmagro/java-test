package com.ford.grocery.checkout;

import java.util.LinkedList;
import java.util.List;

import com.ford.grocery.offer.Discount;
import com.ford.grocery.stock.StockItem;

public class Receipt {

    private double total;

    private List<Discount> discounts = new LinkedList<>();

    public double getTotal() {
        return total;
    }

    public void addItems(final int quantity, final StockItem stockItem) {
        total = total + stockItem.getCost() * quantity;
    }

    public void applyDiscount(final Discount discount) {
        discounts.add(discount);
        this.total = this.total - discount.getDiscountAmount();
    }
}
