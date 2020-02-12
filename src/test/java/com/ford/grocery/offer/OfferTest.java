package com.ford.grocery.offer;

import static com.ford.grocery.stock.ItemUnitType.LOAF;
import static com.ford.grocery.stock.ItemUnitType.SINGLE;
import static com.ford.grocery.stock.ItemUnitType.TIN;
import static java.lang.Math.min;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ford.grocery.ShoppingBasket;
import com.ford.grocery.stock.StockItem;
import org.junit.Test;

public class OfferTest {

    private static final double DELTA = 0.00001;

    /**
     * Tests core logic of soup tins offer (no date validation)
     */
    @Test
    public void testSoupTinsOffer(){
        final Offer soupTinsOffer = Offer
                .newOffer(basket -> basket.hasAtLeast(2, TIN, "soup") && basket.hasAtLeast(1, LOAF, "bread"))
                .forProduct(new StockItem("bread", LOAF, 0.80))
                .withDiscount((basket, discountedProduct) -> {
                    final int numSoupTins = basket.getCount(TIN, "soup");
                    final int numLoafBread = basket.getCount(LOAF, "bread");

                    final int numTimesEligible = numSoupTins / 2;
                    final int numTimesApplicable = min(numTimesEligible, numLoafBread);

                    return new Discount(discountedProduct.getCost() * 0.50 * numTimesApplicable, numTimesApplicable);

                }).build();


        final ShoppingBasket basket = new ShoppingBasket();
        basket.add(2, TIN, "soup");
        basket.add(2, LOAF, "bread");

        assertTrue("offer should apply to the basket", basket.isEligibleFor(soupTinsOffer));
        final Discount discount = soupTinsOffer.calculateDiscount(basket);
        assertEquals(0.40, discount.getTotalDiscountAmount(), DELTA);
        assertEquals(1, discount.getTimesApplied());
    }

    /**
     * Tests core logic of apples offer (no date validation)
     */
    @Test
    public void testApplesOffer(){
        final Offer applesOffer = Offer
                .newOffer(basket -> basket.hasAtLeast(1, SINGLE, "apples"))
                .forProduct(new StockItem("apples", SINGLE, 0.10))
                .withDiscount((basket, discountedProduct) -> {
                    final int numApples = basket.getCount(SINGLE, "apples");

                    final double appleCost = discountedProduct.getCost();
                    return new Discount(appleCost * 0.10 * numApples, numApples);
                }).build();

        final ShoppingBasket basket = new ShoppingBasket();
        basket.add(1, SINGLE, "apples");
        basket.add(2, SINGLE, "apples");

        assertTrue("offer should apply to the basket", basket.isEligibleFor(applesOffer));
        final Discount discount = applesOffer.calculateDiscount(basket);
        assertEquals(0.03, discount.getTotalDiscountAmount(), DELTA);
        assertEquals(3, discount.getTimesApplied());

    }

}
