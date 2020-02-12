package com.ford.grocery.offer;

import java.time.LocalDate;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.ford.grocery.ShoppingBasket;
import com.ford.grocery.stock.StockItem;

public class Offer {

    private Function<LocalDate, Boolean> validity = (localDate -> true);

    private StockItem discountedProduct;

    private final static Discount NO_DISCOUNT = new Discount(0, 0);

    private Function<ShoppingBasket, Boolean> eligibility = basket -> false;

    private BiFunction<ShoppingBasket,StockItem, Discount> discount = ((basketItems, stockItem) -> NO_DISCOUNT);

    public static OfferBuilder newOffer(Function<ShoppingBasket, Boolean> eligibility){
        final Offer offer = new Offer();
        offer.eligibility = eligibility;

        return new OfferBuilder(offer);
    }

    public boolean isApplicableTo(final ShoppingBasket shoppingBasket) {
        return this.eligibility.apply(shoppingBasket)
                && this.validity.apply(shoppingBasket.getShoppingDate());
    }

    public Discount calculateDiscount(final ShoppingBasket basket) {
        return this.discount.apply(basket, discountedProduct);
    }

    public static class OfferBuilder {

        private Offer offer;

        private OfferBuilder(final Offer offer) {
            this.offer = offer;
        }

        public OfferBuilder forProduct(final StockItem stockItem) {
            this.offer.discountedProduct = stockItem;
            return this;
        }

        public OfferBuilder withDiscount(final BiFunction<ShoppingBasket, StockItem, Discount> applicableDiscount) {
            this.offer.discount = applicableDiscount;
            return this;
        }

        public OfferBuilder validFor(final Function<LocalDate, Boolean> validity) {
            this.offer.validity = validity;
            return this;
        }

        public Offer build() {
            return this.offer;
        }
    }

}
