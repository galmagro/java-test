package com.ford.grocery.checkout;

import static java.lang.String.format;

import com.ford.grocery.ShoppingBasket;
import com.ford.grocery.offer.OfferRepository;
import com.ford.grocery.stock.StockItem;
import com.ford.grocery.stock.StockItemRepository;

public class CheckoutService {

    private StockItemRepository stockItemRepository;

    private OfferRepository offerRepository;

    public CheckoutService(final StockItemRepository stockItemRepository,
                           final OfferRepository offerRepository) {
        this.stockItemRepository = stockItemRepository;
        this.offerRepository = offerRepository;
    }

    public Receipt checkout(final ShoppingBasket basket) {
        Receipt receipt = new Receipt();
        for(ShoppingBasket.BasketItem basketItem: basket) {
            //will use the product name as the key
            stockItemRepository
                    .findStockItem(basketItem.getProduct())
                    .ifPresent(stockItem -> this.processItem(stockItem, basketItem, receipt));
        }

        offerRepository.getEligibleOffers(basket).forEach(offer ->
                receipt.applyDiscount(offer.calculateDiscount(basket)));

        return receipt;
    }

    private void processItem(final StockItem stockItem, final ShoppingBasket.BasketItem basketItem, final Receipt receipt ) {
        if(stockItem.getUnit().equals(basketItem.getUnit())){
            receipt.addItems(basketItem.getQuantity(), stockItem);
        } else {
            throw new IllegalArgumentException(format("invalid unit {} for product {}", basketItem.getUnit(), basketItem.getProduct()));
        }
    }

}
