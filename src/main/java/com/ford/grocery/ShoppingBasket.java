package com.ford.grocery;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ShoppingBasket implements Iterable<ShoppingBasket.BasketItem> {

    private List<BasketItem> items = new LinkedList<>();

    public void add(final int quantity, final ItemUnitType unitType, final String product) {
        items.add(new BasketItem(product, unitType, quantity));
    }

    @Override
    public Iterator<BasketItem> iterator() {
        return items.iterator();
    }

    public class BasketItem {
        private String product;
        private ItemUnitType unit;
        private int quantity;

        public BasketItem(final String product, final ItemUnitType unit, final int quantity) {
            this.product = product;
            this.unit = unit;
            this.quantity = quantity;
        }

        public String getProduct() {
            return product;
        }

        public ItemUnitType getUnit() {
            return unit;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
