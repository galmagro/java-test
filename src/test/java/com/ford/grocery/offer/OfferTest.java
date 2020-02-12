package com.ford.grocery.offer;

import static com.ford.grocery.stock.ItemUnitType.LOAF;
import static com.ford.grocery.stock.ItemUnitType.TIN;
import static java.lang.Math.min;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ford.grocery.ShoppingBasket;
import com.ford.grocery.stock.StockItem;
import org.junit.Test;

public class OfferTest {

    private static final double DELTA = 0.00001;

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
        assertEquals(0.40, discount.getDiscountAmount(), DELTA);
        assertEquals(1, discount.getTimesApplied());
    }

}
