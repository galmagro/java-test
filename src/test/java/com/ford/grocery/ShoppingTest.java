package com.ford.grocery;

import org.junit.Assert;
import org.junit.Test;

public class ShoppingTest {

    @Test
    public void testBasketWithoutDiscounts(){
        ShoppingBasket basket = new ShoppingBasket();
        basket.add(1, TIN, "soup");
        basket.add(2, LOAF, "bread");
        basket.add(1, BOTTLE, "milk");

        Checkout checkout = checkoutService.checkout(basket);
        Assert.assertEquals("basket total price doesn't match", 0.65 + (2 * 0.80) + 1.30,  checkout.getTotal());
    }

}
