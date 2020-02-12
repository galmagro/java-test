package com.ford.grocery;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.ford.grocery.offer.Offer;
import com.ford.grocery.stock.ItemUnitType;

public class ShoppingBasket implements Iterable<ShoppingBasket.BasketItem> {

    private Map<BasketKey, Integer> itemCounts = new HashMap<>();

    private LocalDate shoppingDate;

    public ShoppingBasket() {
        this.shoppingDate =  LocalDate.now();
    }

    public ShoppingBasket(LocalDate shoppingDate) {
        this.shoppingDate = shoppingDate;
    }

    public void add(final int quantity, final ItemUnitType unitType, final String product) {
        final BasketKey basketKey = new BasketKey(product, unitType);
        if (itemCounts.containsKey(basketKey)) {
            int currentAmount = itemCounts.get(basketKey);
            itemCounts.put(basketKey, currentAmount + quantity);
        } else {
            itemCounts.put(basketKey, quantity);
        }
    }

    @Override
    public Iterator<BasketItem> iterator() {
        return itemCounts.entrySet()
                .stream()
                .map(itemCountEntry -> {
                    final BasketKey itemKey = itemCountEntry.getKey();
                    return new BasketItem(itemKey.product, itemKey.unitType, itemCountEntry.getValue());
                })
                .iterator();
    }

    public boolean hasAtLeast(final int amount, final ItemUnitType unitType, final String product) {
        final BasketKey basketKey = new BasketKey(product, unitType);
        return itemCounts.containsKey(basketKey) && itemCounts.get(basketKey) >= amount;
    }

    public int getCount(final ItemUnitType unitType, final String product) {
        final BasketKey basketKey = new BasketKey(product, unitType);
        return Optional.ofNullable(itemCounts.get(basketKey)).orElse(0);
    }

    /**
     * Indicates if the current shopping basket is eligible for the provided offer.
     * Method created just for the sake of readability of code ...
     * @param offer candidadte offer
     * @return boolean indicating if the basket applies to the offer
     */
    public boolean isEligibleFor(final Offer offer) {
        return offer.isApplicableTo(this);
    }

    public LocalDate getShoppingDate() {
        return shoppingDate;
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

    private class BasketKey {

        private String product;

        private ItemUnitType unitType;

        public BasketKey(final String product, final ItemUnitType unitType) {
            this.product = product;
            this.unitType = unitType;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final BasketKey basketKey = (BasketKey) o;
            return product.equals(basketKey.product) &&
                    unitType == basketKey.unitType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(product, unitType);
        }
    }
}
