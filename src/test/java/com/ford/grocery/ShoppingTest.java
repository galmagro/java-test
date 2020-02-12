package com.ford.grocery;

import static com.ford.grocery.stock.ItemUnitType.BOTTLE;
import static com.ford.grocery.stock.ItemUnitType.LOAF;
import static com.ford.grocery.stock.ItemUnitType.SINGLE;
import static com.ford.grocery.stock.ItemUnitType.TIN;
import static org.junit.Assert.assertEquals;

import com.ford.grocery.checkout.CheckoutService;
import com.ford.grocery.checkout.Receipt;

import com.ford.grocery.offer.memory.InMemoryOfferRepository;
import com.ford.grocery.stock.json.JsonStockItemRepository;
import org.junit.Before;
import org.junit.Test;

public class ShoppingTest {

    private static final double DELTA = 0.00001;

    private CheckoutService checkoutService;

    @Before
    public void prepareTests(){
        this.checkoutService = new CheckoutService(new JsonStockItemRepository(), new InMemoryOfferRepository());
    }

    @Test
    public void shouldCalculateTotalWithoutDiscounts(){
        ShoppingBasket basket = new ShoppingBasket();
        basket.add(1, TIN, "soup");
        basket.add(2, LOAF, "bread");
        basket.add(1, BOTTLE, "milk");

        Receipt receipt = checkoutService.checkout(basket);
        assertEquals("basket total price doesn't match", 0.65 + (2 * 0.80) + 1.30,  receipt.getTotal(), DELTA);
    }

    @Test
    public void shouldCalculateTotalWithoutDiscountsWhenAddingSameProductMultipleTimes(){
        ShoppingBasket basket = new ShoppingBasket();
        basket.add(1, LOAF, "bread");
        basket.add(1, BOTTLE, "milk");
        basket.add(1, LOAF, "bread");

        Receipt receipt = checkoutService.checkout(basket);
        assertEquals("basket total price doesn't match", (2 * 0.80) + 1.30,  receipt.getTotal(), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptInvalidProductUnits(){
        ShoppingBasket basket = new ShoppingBasket();
        basket.add(1, LOAF, "bread");
        basket.add(1, TIN, "milk");

        checkoutService.checkout(basket);
    }

    @Test
    public void shouldApplyDiscountForSoupTins(){
        ShoppingBasket basket = new ShoppingBasket();
        basket.add(3, TIN, "soup");
        basket.add(2, LOAF, "bread");

        Receipt receipt = checkoutService.checkout(basket);
        assertEquals("basket total price doesn't apply soup discounts correctly", 3.15, receipt.getTotal(), DELTA);
    }

    @Test
    public void shouldApplyDiscountForApples(){
        ShoppingBasket basket = new ShoppingBasket();
        basket.add(6, SINGLE, "apples");
        basket.add(1, BOTTLE, "milk");

        Receipt receipt = checkoutService.checkout(basket);
        assertEquals("basket total price doesn't apply apple discounts correctly", 1.90, receipt.getTotal(), DELTA);

    }

}
