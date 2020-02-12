package com.ford.grocery.checkout;

import static java.util.Collections.unmodifiableSet;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ford.grocery.offer.Discount;
import com.ford.grocery.stock.StockItem;

public class Receipt {

    private double total;

    private Set<Discount> discounts = new LinkedHashSet<>();

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

    public Set<Discount> getDiscounts() {
        return unmodifiableSet(discounts);
    }
}
