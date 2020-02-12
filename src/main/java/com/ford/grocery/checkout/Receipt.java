package com.ford.grocery.checkout;

import static java.util.Collections.unmodifiableSet;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.ford.grocery.offer.Discount;
import com.ford.grocery.stock.StockItem;

public class Receipt implements Iterable<Receipt.Line> {

    private Set<Discount> discounts = new LinkedHashSet<>();

    private Set<Line> lines = new LinkedHashSet<>();

    Receipt(){}

    public double getTotal() {
        return getGrossTotal() - getDiscountTotal();
    }

    public double getDiscountTotal() {
        return getDiscounts().stream()
                .mapToDouble(Discount::getTotalDiscountAmount)
                .sum();
    }

    void addItems(final int quantity, final StockItem stockItem) {
        lines.add(new Line(quantity, stockItem));
    }

    public void applyDiscount(final Discount discount) {
        discounts.add(discount);
    }

    @Override
    public Iterator<Line> iterator() {
        return this.lines.iterator();
    }

    public Set<Discount> getDiscounts() {
        return unmodifiableSet(discounts);
    }

    public double getGrossTotal() {
        return this.lines.stream()
                .mapToDouble(Line::lineTotal)
                .sum();
    }

    public class Line {

        private int quantity;

        private StockItem stockItem;

        public Line(final int quantity, final StockItem stockItem) {
            this.quantity = quantity;
            this.stockItem = stockItem;
        }

        public int getQuantity() {
            return quantity;
        }

        public StockItem getStockItem() {
            return stockItem;
        }

        public double lineTotal() {
            return stockItem.getCost() * quantity;
        }
    }
}
