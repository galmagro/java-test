package com.ford.grocery.console;

import java.io.PrintStream;

import com.ford.grocery.ShoppingBasket;

public class ShoppingBasketPrinter extends ShoppingPrinter<ShoppingBasket> {

    public ShoppingBasketPrinter(final ShoppingBasket printedItem) {
        super(printedItem);
    }

    public void printTo(final PrintStream stream) {
        for(ShoppingBasket.BasketItem basketItem: printedItem) {
            stream.printf("%d %s %s\n", basketItem.getQuantity(), basketItem.getUnit().name().toLowerCase(), basketItem.getProduct());
        }
    }
}
