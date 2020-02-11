package com.ford.grocery;

import static com.ford.grocery.ItemUnitType.BOTTLE;
import static com.ford.grocery.ItemUnitType.LOAF;
import static com.ford.grocery.ItemUnitType.TIN;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ShoppingTest {

    private CheckoutService checkoutService;

    @Test
    public void testBasketWithoutDiscounts(){
        ShoppingBasket basket = new ShoppingBasket();
        basket.add(1, TIN, "soup");
        basket.add(2, LOAF, "bread");
        basket.add(1, BOTTLE, "milk");

        Receipt receipt = checkoutService.checkout(basket);
        assertEquals("basket total price doesn't match", 0.65 + (2 * 0.80) + 1.30,  receipt.getTotal());
    }

}