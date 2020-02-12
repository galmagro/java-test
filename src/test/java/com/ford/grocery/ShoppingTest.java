package com.ford.grocery;

import static com.ford.grocery.stock.ItemUnitType.BOTTLE;
import static com.ford.grocery.stock.ItemUnitType.LOAF;
import static com.ford.grocery.stock.ItemUnitType.SINGLE;
import static com.ford.grocery.stock.ItemUnitType.TIN;
import static java.time.LocalDate.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import com.ford.grocery.checkout.CheckoutService;
import com.ford.grocery.checkout.Receipt;
import com.ford.grocery.offer.Discount;
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
    public void shouldNotApplyDiscountForApples(){
        ShoppingBasket basket = new ShoppingBasket();
        basket.add(6, SINGLE, "apples");
        basket.add(1, BOTTLE, "milk");

        Receipt receipt = checkoutService.checkout(basket);
        assertEquals("basket total price shouldn't be applying apple discounts", 1.90, receipt.getTotal(), DELTA);
        assertTrue("receipt should not have any discounts", receipt.getDiscounts().isEmpty());

    }

    @Test
    public void shouldApplyDiscountForApplesIn5DaysTime(){
        ShoppingBasket basket = new ShoppingBasket(now().plusDays(5));
        basket.add(6, SINGLE, "apples");
        basket.add(1, BOTTLE, "milk");

        Receipt receipt = checkoutService.checkout(basket);
        assertEquals("basket total price should be applying apple discounts", 1.84, receipt.getTotal(), DELTA);
        assertEquals("receipt should reflect a discount", 1, receipt.getDiscounts().size());
        final Discount discount = receipt.getDiscounts().iterator().next();
        assertEquals("discount amount should be 0.06", 0.06, discount.getDiscountAmount(), DELTA);
    }

    @Test
    public void shouldApplyDiscountsForApplesAndTinsOfSoupIn5DaysTime(){
        ShoppingBasket basket = new ShoppingBasket(now().plusDays(5));
        basket.add(3, SINGLE, "apples");
        basket.add(2, TIN, "soup");
        basket.add(1, LOAF, "bread");

        Receipt receipt = checkoutService.checkout(basket);
        assertEquals("basket total price should be applying expected discounts", 1.97, receipt.getTotal(), DELTA);
        assertEquals("receipt should reflect two discounts", 2, receipt.getDiscounts().size());
        final Iterator<Discount> discountIterator = receipt.getDiscounts().iterator();
        final Discount discount1 = discountIterator.next();
        final Discount discount2 = discountIterator.next();
        assertEquals("discount amount for bread should be 0.4", 0.4, discount1.getDiscountAmount(), DELTA);
        assertEquals("discount amount for apples should be 0.3", 0.3, discount2.getDiscountAmount(), DELTA);
    }

}
