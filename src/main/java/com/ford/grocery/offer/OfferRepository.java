package com.ford.grocery.offer;

import java.util.List;
import java.util.Set;

import com.ford.grocery.ShoppingBasket;

public interface OfferRepository {

    Set<Offer> getEligibleOffers(ShoppingBasket basket);

}
