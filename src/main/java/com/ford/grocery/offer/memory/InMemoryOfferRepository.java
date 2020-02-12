package com.ford.grocery.offer.memory;

import static com.ford.grocery.stock.ItemUnitType.LOAF;
import static com.ford.grocery.stock.ItemUnitType.SINGLE;
import static com.ford.grocery.stock.ItemUnitType.TIN;
import static java.lang.Math.min;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.ford.grocery.ShoppingBasket;
import com.ford.grocery.offer.Discount;
import com.ford.grocery.offer.Offer;
import com.ford.grocery.offer.OfferRepository;
import com.ford.grocery.stock.StockItem;

public class InMemoryOfferRepository implements OfferRepository {

    private final Set<Offer> OFFERS = new HashSet<>();

    public InMemoryOfferRepository() {

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

        final Offer applesOffer = Offer
                .newOffer(basket -> basket.hasAtLeast(1, SINGLE, "apples"))
                .forProduct(new StockItem("apples", SINGLE, 0.10))
                .withDiscount((basket, discountedProduct) -> {
                    final int numApples = basket.getCount(SINGLE, "apples");

                    final double appleCost = discountedProduct.getCost();
                    return new Discount(appleCost * 0.10 * numApples, numApples);
                }).build();

        OFFERS.add(soupTinsOffer);
        OFFERS.add(applesOffer);


    }

    @Override
    public Set<Offer> getEligibleOffers(final ShoppingBasket basket) {
        return OFFERS.stream()
                .filter(offer -> offer.isApplicableTo(basket))
                .collect(Collectors.toSet());
    }
}
